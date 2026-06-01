package com.cts.edutrack.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cts.edutrack.dto.ApiResponse;

@FeignClient(name="ENROLLMENT-SERVICE",path="/api/attendance")
public interface EnrollmentClient {
	@PostMapping("/markAttendance/{userId}")
	public ApiResponse markAttendance(@PathVariable Long userId);
	

}
