package com.cts.edutrack.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Course {
    private Long courseId; 
    private String name; 
    private String description; 
    private Integer creditPoints;
    private Status status;
    private Program program;
    private List<Module> modules;

    public enum Status { ACTIVE, INACTIVE } 
}