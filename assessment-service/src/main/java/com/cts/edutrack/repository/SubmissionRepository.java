package com.cts.edutrack.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.edutrack.model.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

	boolean existsByAssessmentIdAndUserId(long assessmentId, long userId);

	Optional<Submission> findByAssessmentIdAndUserId(long assessmentId, long userId);
	
	// Add this below your existing methods
	List<Submission> findByUserId(Long userId);

	// Average by user (local field)
	@Query("""
			    SELECT AVG(s.score)
			    FROM Submission s
			    WHERE s.userId = :userId
			""")
	Double findAverageScoreByUserId(@Param("userId") Long userId);

	// Average by course (Submission + Assessment only)
	@Query("""
			    SELECT AVG(s.score)
			    FROM Submission s, Assessment a
			    WHERE s.assessmentId = a.assessmentId
			      AND a.courseId = :courseId
			""")
	Double findAverageScoreByCourseId(@Param("courseId") Long courseId);

	// Average by multiple courses (for program-level aggregation via service)
	@Query("""
			    SELECT AVG(s.score)
			    FROM Submission s, Assessment a
			    WHERE s.assessmentId = a.assessmentId
			      AND a.courseId IN :courseIds
			""")
	Double findAverageScoreByCourseIds(@Param("courseIds") Collection<Long> courseIds);

	// User’s average within a set of courses (for program filter)
	@Query("""
			    SELECT AVG(s.score)
			    FROM Submission s, Assessment a
			    WHERE s.assessmentId = a.assessmentId
			      AND s.userId = :userId
			      AND a.courseId IN :courseIds
			""")
	Double findUserAverageByCourseIds(@Param("userId") Long userId, @Param("courseIds") Collection<Long> courseIds);

}