package com.cts.edutrack.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ENROLLMENT-SERVICE", path = "/api/enrollments", configuration=FeignConfig.class)
public interface EnrollmentClient {
	@GetMapping("/checkUserIdAndProgram")
	public boolean checkEnrollmentByUserIdAndProgramId(Long userId, Long programId);
}
