package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cts.edutrack.client.CourseClient;
import com.cts.edutrack.client.EnrollmentClient;
import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.EnrollmentResponse;
import com.cts.edutrack.dto.ModuleProgramDto;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.Content;
import com.cts.edutrack.repository.ContentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;
    private final CourseClient courseClient;
    private final EnrollmentClient enrollmentClient;
    private final ObjectMapper objectMapper;

    private Long resolveProgramIdByModule(Long moduleId) {
        ApiResponse response = courseClient.getProgramIdByModule(moduleId);
        // Ensure the mapping matches the key "programId" from the controller
        ModuleProgramDto dto = objectMapper.convertValue(response.getData(), ModuleProgramDto.class);
        
        if (dto == null || dto.programId() == null) {
            throw new NotFoundException("Could not resolve Program for Module: " + moduleId);
        }
        return dto.programId();
    }

//    private boolean hasActiveEnrollment(Long userId, Long programId) {
////        Boolean result = enrollmentClient.hasActiveEnrollment(userId, programId, "Active");
//        @Nullable
//		ApiResponse response = enrollmentClient.getByProgram(programId).getBody();
//	     EnrollmentResponse enrollmentResponse = objectMapper.convertValue(response.getData(), EnrollmentResponse.class);
//	     
//	     Boolean result=enrollmentResponse.getStatus().equals("ACTIVE");
//        
//        return result;
//    }

    public ApiResponse getContentById(long contentId, Long currentUserId, String role) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NotFoundException("Content not found with ID: " + contentId));

        // INSTRUCTOR and ADMIN can access directly
        if ("INSTRUCTOR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            return new ApiResponse(true, "Content fetched successfully", content,
                    HttpStatus.OK.value(), Collections.emptyList());
        }

        // STUDENT needs enrollment validation
        Long programId = resolveProgramIdByModule(content.getModuleId());
        
        // Use the explicit Boolean check endpoint
        Boolean isEnrolled = enrollmentClient.hasActiveEnrollment(currentUserId, programId, "ACTIVE");

        if (!isEnrolled) {
            return new ApiResponse(false, "Access Denied: Enrollment Required", null, 403, null);
        }

        return new ApiResponse(true, "Success", content, 200, null);
    }

    public ApiResponse getContentByModuleId(long moduleId, Long currentUserId, String role) {
        // INSTRUCTOR and ADMIN can access directly
        if ("INSTRUCTOR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            List<Content> contents = contentRepository.findByModuleId(moduleId);
            return new ApiResponse(true, "Content list fetched successfully", contents,
                    HttpStatus.OK.value(), Collections.emptyList());
        }

        // STUDENT needs enrollment validation
        Long programId = resolveProgramIdByModule(moduleId);
        
        // Use the explicit Boolean check endpoint
        Boolean isEnrolled = enrollmentClient.hasActiveEnrollment(currentUserId, programId, "ACTIVE");

        if (!isEnrolled) {
            return new ApiResponse(false, "Access Denied: Enrollment Required", null, 403, null);
        }
        List<Content> contents = contentRepository.findByModuleId(moduleId);
        return new ApiResponse(true, "Content list fetched successfully", contents,
                HttpStatus.OK.value(), Collections.emptyList());
    }

    public ApiResponse saveContent(Content content) {
        Content saved = contentRepository.save(content);
        return new ApiResponse(true, "Content Saved Successfully", saved,
                HttpStatus.CREATED.value(), Collections.emptyList());
    }

    public ApiResponse saveContentByModule(long moduleId, Content content) {
        content.setModuleId(moduleId);
        Content saved = contentRepository.save(content);
        return new ApiResponse(true, "Content Saved for Module " + moduleId, saved,
                HttpStatus.CREATED.value(), Collections.emptyList());
    }

    public ApiResponse deleteContent(long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NotFoundException("Content Not Found with ID: " + contentId));
        contentRepository.delete(content);
        return new ApiResponse(true, "Content Deleted Successfully", content,
                HttpStatus.OK.value(), Collections.emptyList());
    }

    public ApiResponse updateContent(long contentId, Content updatedContent) {
        Content existing = contentRepository.findById(contentId)
                .orElseThrow(() -> new NotFoundException("Content not found with ID: " + contentId));

        if (!existing.getModuleId().equals(updatedContent.getModuleId())) {
            return new ApiResponse(false, "ModuleId cannot be changed once content is created",
                    null, HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
        }
        modelMapper.map(updatedContent, existing);
        
        existing.setContentId(contentId);
        
        Content saved = contentRepository.save(existing);
        return new ApiResponse(true, "Content Updated Successfully", saved,
                HttpStatus.OK.value(), Collections.emptyList());
    }
}