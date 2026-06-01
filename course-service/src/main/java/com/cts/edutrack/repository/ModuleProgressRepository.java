package com.cts.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.edutrack.model.ModuleProgress;

public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {
    long countByUserIdAndCourseId(Long userId, Long courseId);
    List<ModuleProgress> findByUserIdAndCourseId(Long userId, Long courseId);
    boolean existsByUserIdAndModuleId(Long userId, Long moduleId);
}