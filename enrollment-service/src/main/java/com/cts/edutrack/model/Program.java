package com.cts.edutrack.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Program {
    private Long programId; 
    private String name; 
    private String description; 
    private Integer durationWeeks; 
    private Status status; 
    private List<Course> courses;
    public enum Status { ACTIVE, INACTIVE }
}