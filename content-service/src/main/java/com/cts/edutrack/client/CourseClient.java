package com.cts.edutrack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.ModuleProgramDto;
import com.cts.edutrack.client.FeignConfig;

@FeignClient(name = "course-service", path = "/api/modules", configuration=FeignConfig.class)
public interface CourseClient {
    
    @GetMapping("/{id}/program-id")
    ApiResponse getProgramIdByModule(@PathVariable Long id);
}
