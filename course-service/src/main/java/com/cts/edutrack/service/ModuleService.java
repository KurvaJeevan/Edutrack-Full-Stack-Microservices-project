package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.ApiResponse;
import com.cts.edutrack.model.Course;
import com.cts.edutrack.model.Module;
import com.cts.edutrack.repository.CourseRepository;
import com.cts.edutrack.repository.ModuleRepository;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository, ModelMapper modelMapper) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    // POST: Add Module to a specific Course
    public ApiResponse addModuleToCourse(Long courseId, Module module) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + courseId));

        module.setCourse(course); // Link module to parent course
        Module saved = moduleRepository.save(module);
        
        return new ApiResponse(true, "Module Saved Successfully", saved, HttpStatus.OK.value(), Collections.emptyList());
    }

    // GET: List all modules for a Course in sequence
    public ApiResponse getModulesForCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new NotFoundException("Course not found with ID: " + courseId);
        }
        // Ordered by sequenceOrder as per requirements 
        List<Module> modules = moduleRepository.findByCourse_CourseIdOrderBySequenceOrderAsc(courseId);
        return new ApiResponse(true, "Modules Fetched Successfully", modules, HttpStatus.OK.value(), Collections.emptyList());
    }

    // GET: Specific Module Details
    public ApiResponse getModuleById(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found with ID: " + moduleId));
        return new ApiResponse(true, "Module Fetched Success", module, HttpStatus.OK.value(), Collections.emptyList());
    }

    // PUT: Update Module (using ModelMapper for consistency)
    public ApiResponse updateModule(Long moduleId, Module updatedModule) {
        Module existing = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found with ID: " + moduleId));
       
        Course c  = existing.getCourse();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(updatedModule, existing);
        existing.setModuleId(moduleId); // Maintain ID integrity
        
        existing.setCourse(c);

        Module saved = moduleRepository.save(existing);
        return new ApiResponse(true, "Module Updated Successfully", saved, HttpStatus.OK.value(), Collections.emptyList());
    }

    // DELETE: Remove Module
    public ApiResponse deleteModule(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found with ID: " + moduleId));
        
        moduleRepository.delete(module);
        return new ApiResponse(true, "Module Deleted Successfully", null, HttpStatus.OK.value(), Collections.emptyList());
    }
}