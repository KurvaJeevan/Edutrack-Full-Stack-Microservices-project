package com.cts.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.edutrack.model.Question;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Query("select q.question from Question q where q.courseId = :cid")
    List<String> findAllQuestionTextsByCourseId(@Param("cid") Long cid);

	boolean existsByCourseIdAndQuestion(long courseId, String question);
	
	List<Question> findAllByCourseId(Long courseId);

}
