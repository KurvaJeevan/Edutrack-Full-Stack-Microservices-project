package com.cts.edutrack.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgressStatusResponse {
    private long completedModules;
    private int totalModules;
    private boolean canTakeAssessment;
    private boolean isCourseCompleted; // From CourseCompletion table
}