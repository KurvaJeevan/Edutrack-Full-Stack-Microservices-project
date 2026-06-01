package com.cts.edutrack.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_completion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long programId;
    private Long courseId;
    
    private Double assessmentScore;
    private boolean isPassed;
    private LocalDateTime completedAt = LocalDateTime.now();

    public CourseCompletion(Long userId, Long programId, Long courseId, Double score, boolean isPassed) {
        this.userId = userId;
        this.programId = programId;
        this.courseId = courseId;
        this.assessmentScore = score;
        this.isPassed = isPassed;
    }
}