package com.cts.edutrack.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.edutrack.dto.ApiResponse;

@FeignClient(name = "COURSE-SERVICE", path = "/api", configuration=FeignConfig.class)
public interface CourseClient {
    @GetMapping("/courses/{courseId}")
    ApiResponse getCourseById(@PathVariable Long courseId);
    
    @GetMapping("/programs/{programId}/courses")
    public ApiResponse getCoursesByProgram(@PathVariable Long programId);
}