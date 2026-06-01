package com.cts.edutrack.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response DTO for Enrollment.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private long enrollmentId;
    private long programId;
    private long userId;
    private LocalDateTime enrolledDate;
    private String status;
}

 