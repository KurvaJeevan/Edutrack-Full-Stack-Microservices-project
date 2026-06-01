package com.cts.edutrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.edutrack.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(Long courseId);
    boolean existsByCourseIdAndStatus(Long courseId, String status);
	List<Course> findByProgram_ProgramId(Long programId);
}
