package com.cts.edutrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    private Long moduleId;
    private Course course; 
    private String name; 
    private Integer sequenceOrder; 
    private String learningObjectives; 
}