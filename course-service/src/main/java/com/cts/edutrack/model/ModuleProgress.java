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
@Table(name = "module_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long courseId;
    private Long moduleId;
    
    private LocalDateTime completedAt = LocalDateTime.now();

    public ModuleProgress(Long userId, Long courseId, Long moduleId) {
        this.userId = userId;
        this.courseId = courseId;
        this.moduleId = moduleId;
    }
}