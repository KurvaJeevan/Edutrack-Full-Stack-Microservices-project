package com.cts.edutrack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.edutrack.dto.ApiResponse;


@FeignClient(name = "enrollment-service", path = "/api/enrollments" , configuration=FeignConfig.class)
public interface EnrollmentClient {
    
    @GetMapping("/exists")
    Boolean hasActiveEnrollment(@RequestParam Long userId, 
                                @RequestParam Long programId, 
                                @RequestParam String status);
    
    @GetMapping("/by-Program/{programId}")
	public ResponseEntity<ApiResponse> getByProgram(@PathVariable long programId) ;
}
