package com.cts.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.cts.edutrack.model.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourse_CourseIdOrderBySequenceOrderAsc(Long courseId);
    
    
}