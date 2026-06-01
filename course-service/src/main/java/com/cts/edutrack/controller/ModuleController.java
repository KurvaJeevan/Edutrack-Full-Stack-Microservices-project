 package com.cts.edutrack.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
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
import com.cts.edutrack.model.Module;
import com.cts.edutrack.model.Program;
import com.cts.edutrack.service.ModuleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/modules")
    public ApiResponse addModule(@PathVariable Long courseId,@Valid @RequestBody Module module) {
        return moduleService.addModuleToCourse(courseId, module);
    }

   @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/courses/{courseId}/modules")
    public ApiResponse getModulesByCourse(@PathVariable Long courseId) {
        return moduleService.getModulesForCourse(courseId);
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/modules/{moduleId}")
    public ApiResponse getModuleById(@PathVariable Long moduleId) {
        return moduleService.getModuleById(moduleId);
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PutMapping("/modules/{moduleId}")
    public ApiResponse updateModule(@PathVariable Long moduleId,@Valid @RequestBody Module updatedModule) {
        return moduleService.updateModule(moduleId, updatedModule);
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @DeleteMapping("/modules/{moduleId}")
    public ApiResponse deleteModule(@PathVariable Long moduleId) {
        return moduleService.deleteModule(moduleId);
    }
    
    
    
   @GetMapping("/modules/{id}/program-id")
   public ApiResponse getProgramIdByModule(@PathVariable Long id) {
       Module module = (Module) moduleService.getModuleById(id).getData();
       Long programId = module.getCourse().getProgram().getProgramId(); 
       return new ApiResponse(true, "Fetched", Collections.singletonMap("programId", programId), 200, null);
   }
    
}