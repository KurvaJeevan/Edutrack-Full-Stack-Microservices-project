package com.cts.edutrack.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.edutrack.dto.ApiResponse;

@FeignClient(name = "AUTHENTICATION-SERVICE", path = "/api/users", configuration=FeignConfig.class)
public interface UserClient {
	@GetMapping("/getUser/{id}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id);
}
