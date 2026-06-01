package com.cts.edutrack.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.edutrack.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByProgramId(Long programId);

    List<Enrollment> findByUserId(Long userId);
    boolean existsByUserIdAndProgramId(Long userId, Long programId);

    boolean existsByProgramIdAndUserIdAndStatusIn(long programId, long userId, Collection<String> statuses);
 
    //
    boolean existsByUserIdAndProgramIdAndStatus(Long userId, Long programId, String status);
}