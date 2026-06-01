package com.cts.edutrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId; 

    @NotBlank(message = "Course name is required")
    private String name; 

    private String description; 

    @NotNull(message = "Credit points are required")
    @Min(value = 0, message = "Credit points cannot be negative")
    private Integer creditPoints;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    @JsonBackReference
    private Program program;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Module> modules;

    public enum Status { ACTIVE, INACTIVE } 
}