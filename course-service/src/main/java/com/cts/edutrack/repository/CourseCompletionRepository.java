package com.cts.edutrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.edutrack.model.CourseCompletion;

public interface CourseCompletionRepository extends JpaRepository<CourseCompletion, Long> {
    long countByUserIdAndProgramIdAndIsPassed(Long userId, Long programId, boolean isPassed);
    Optional<CourseCompletion> findByUserIdAndCourseId(Long userId, Long courseId);
}