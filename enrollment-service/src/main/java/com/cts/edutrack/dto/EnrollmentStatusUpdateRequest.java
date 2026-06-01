package com.cts.edutrack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for updating enrollment status.
 */
@Getter
@Setter
public class EnrollmentStatusUpdateRequest {
    @NotBlank(message = "status is required and cannot be blank")
    private String status; // Allowed: Active, Completed, Dropped
}