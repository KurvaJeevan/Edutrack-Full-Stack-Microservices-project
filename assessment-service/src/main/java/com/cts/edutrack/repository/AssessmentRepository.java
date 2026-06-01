package com.cts.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.edutrack.model.Assessment;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

	boolean existsByCourseId(Long courseId);

	Assessment findByCourseId(Long courseId);

}
