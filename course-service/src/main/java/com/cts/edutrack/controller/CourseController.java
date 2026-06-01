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
import com.cts.edutrack.model.Course;
import com.cts.edutrack.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping("/programs/{programId}/courses")
    public ApiResponse addCourse(@PathVariable Long programId, @RequestBody @Valid Course course) {
        return courseService.addCourseToProgram(programId, course);
    }
    
   @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/programs/{programId}/courses")
    public ApiResponse getCoursesByProgram(@PathVariable Long programId) {
        return courseService.getCoursesByProgram(programId);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/courses/{courseId}")
    public ApiResponse getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PutMapping("/courses/{courseId}")
    public ApiResponse updateCourse(@PathVariable Long courseId, @RequestBody @Valid Course updatedCourse) {
        return courseService.updateCourse(courseId, updatedCourse);
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @DeleteMapping("/courses/{courseId}")
    public ApiResponse deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteCourse(courseId);
    }
}