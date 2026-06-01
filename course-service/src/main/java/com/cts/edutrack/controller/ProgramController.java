package com.cts.edutrack.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.model.ApiResponse;
//import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.model.Program;
import com.cts.edutrack.service.ProgramService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }
    
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping
    public ApiResponse createProgram(@Valid @RequestBody Program program) {
        return programService.createProgram(program);
    }
    
  @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping
    public ApiResponse getAllPrograms() {
        return programService.getAllPrograms();
    }
    
    
  @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/{id}")
    public ApiResponse getProgramById(@PathVariable Long id) {
        return programService.getProgramById(id);
    }
    
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PutMapping("/{id}")
    public ApiResponse updateProgram(@PathVariable Long id,@Valid @RequestBody Program program) {
        return programService.updateProgram(id, program);
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteProgram(@PathVariable Long id) {
        return programService.deleteProgram(id);
    }
}