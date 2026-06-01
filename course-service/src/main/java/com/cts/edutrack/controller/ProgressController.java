package com.cts.edutrack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.model.ApiResponse;
import com.cts.edutrack.repository.ProgramProgressResponse;
import com.cts.edutrack.repository.ProgressStatusResponse;
import com.cts.edutrack.service.ProgressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    
    @GetMapping("/module-status/course/{courseId}/module/{moduleId}/user/{userId}")
    public ResponseEntity<ApiResponse> isModuleCompleted(@PathVariable Long courseId, @PathVariable Long userId, @PathVariable Long moduleId) {
        if(progressService.isModuleCompleted(userId, courseId, moduleId)){
        	return ResponseEntity.ok(new ApiResponse(true, "Module Completed", null, 200, null));
        }
        
        return ResponseEntity.ok(new ApiResponse(false, "module not completed", null, 200, null));
    }
    
    @PostMapping("/module-complete")
    public ResponseEntity<ApiResponse> completeModule(@RequestParam Long userId, 
                                                      @RequestParam Long courseId, 
                                                      @RequestParam Long moduleId) {
        progressService.markModuleComplete(userId, courseId, moduleId);
        return ResponseEntity.ok(new ApiResponse(true, "Module Progress Saved", null, 200, null));
    }

    @GetMapping("/course-status/{courseId}/user/{userId}")
    public ResponseEntity<ApiResponse> getStatus(@PathVariable Long courseId, @PathVariable Long userId) {
        ProgressStatusResponse data = progressService.getCourseProgress(userId, courseId);
        return ResponseEntity.ok(new ApiResponse(true, "Progress Fetched", data, 200, null));
    }

    // This would be called by your Assessment Service internally or via Gateway
    @PostMapping("/assessment-pass")
    public ResponseEntity<ApiResponse> completeCourse(@RequestParam Long userId, 
                                                      @RequestParam Long courseId, 
                                                      @RequestParam Double score) {
        ProgramProgressResponse data = progressService.processAssessmentPass(userId, courseId, score);
        return ResponseEntity.ok(new ApiResponse(true, "Assessment Recorded", data, 200, null));
    }

    // Called by Dashboard to show Program Progress bars
    @GetMapping("/program-status/{programId}/user/{userId}")
    public ResponseEntity<ApiResponse> getProgramStatus(@PathVariable Long programId, @PathVariable Long userId) {
        ProgramProgressResponse data = progressService.getProgramProgress(userId, programId);
        return ResponseEntity.ok(new ApiResponse(true, "Program Progress Fetched", data, 200, null));
    }
}