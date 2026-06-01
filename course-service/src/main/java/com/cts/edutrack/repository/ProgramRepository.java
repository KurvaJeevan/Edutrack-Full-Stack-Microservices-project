package com.cts.edutrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.edutrack.model.Program;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByStatus(Program.Status status);
    
    List<String> findByNameContainingIgnoreCase(String name);
}