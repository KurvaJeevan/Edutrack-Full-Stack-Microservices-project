package com.cts.edutrack.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramProgressResponse {
    private Long programId;
    private long completedCourses;
    private int totalCourses;
    private double completionPercentage; // (completed/total) * 100
    private boolean isProgramCompleted;
}