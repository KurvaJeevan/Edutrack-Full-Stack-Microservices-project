package com.cts.edutrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Entity
@Table(name = "modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false) 
    @JsonBackReference
    private Course course; 
    
    @NotBlank(message = "Module name is required") 
    private String name; 

    @NotNull(message = "Sequence order is required") 
    @Min(value = 1, message = "Sequence order must start from 1")
    private Integer sequenceOrder; 

    @NotBlank(message = "Learning objectives are required") 
    private String learningObjectives; 
}