package com.cts.edutrack.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.edutrack.dto.ApiResponse;

@FeignClient(name = "COURSE-SERVICE", path = "/api/programs",configuration = FeignConfig.class)
public interface ProgramClient {
	@GetMapping("/{id}")
    public ApiResponse getProgramById(@PathVariable Long id);
}