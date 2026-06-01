package com.cts.edutrack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cts.edutrack.client.UserClient;
import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.Content;
import com.cts.edutrack.model.User;
import com.cts.edutrack.service.ContentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/{contentId}")
    public ApiResponse getContentById(@PathVariable long contentId, Authentication authentication) {
        String email = authentication.getName();
        ResponseEntity<ApiResponse> responseEntity = userClient.getUserByEmail(email);
        
        ApiResponse apiResponse = responseEntity.getBody();
        
        if (apiResponse == null || apiResponse.getData() == null) {
            throw new NotFoundException("User data not found for email: " + email);
        }

        User user = objectMapper.convertValue(apiResponse.getData(), User.class);
        
        System.err.println("User Email: " + email);
        System.err.println("User ID: " + user.getUserId());
        
        return contentService.getContentById(contentId, user.getUserId(), user.getRole());
    }

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping
    public ApiResponse saveContent(@Valid @RequestBody Content content) {
        return contentService.saveContent(content);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping("/module/{moduleId}")
    public ApiResponse saveContentByModule(@PathVariable long moduleId, @Valid @RequestBody Content content) {
        return contentService.saveContentByModule(moduleId, content);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
    @GetMapping("/module/{moduleId}")
    public ApiResponse getContentByModuleId(@PathVariable long moduleId, Authentication authentication) {
        String email = authentication.getName();
        
        ResponseEntity<ApiResponse> response = userClient.getUserByEmail(email);
        
        User user = objectMapper.convertValue(response.getBody().getData(), User.class);
        
        return contentService.getContentByModuleId(moduleId, user.getUserId(), user.getRole());
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @DeleteMapping("/{contentId}")
    public ApiResponse deleteContent(@PathVariable long contentId) {
        return contentService.deleteContent(contentId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PutMapping("/{contentId}")
    public ApiResponse updateContent(@PathVariable long contentId, @RequestBody Content content) {
        return contentService.updateContent(contentId, content);
    }
}