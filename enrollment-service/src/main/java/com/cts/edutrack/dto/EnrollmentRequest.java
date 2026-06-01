package com.cts.edutrack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for creating an enrollment.
 * Validations are at controller level (as per your instruction).
 */
@Getter
@Setter
public class EnrollmentRequest {

    @NotNull(message = "programId is required")
    @Min(value = 1, message = "programId must be a positive number")
    private Long programId;

    @NotNull(message = "userId is required")
    @Min(value = 1, message = "userId must be a positive number")
    private Long userId;
}