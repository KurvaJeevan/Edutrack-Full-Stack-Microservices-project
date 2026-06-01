package com.cts.edutrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "programs")
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId; 

    
    
    @NotBlank(message = "Program name is required") 
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name; 

    @NotBlank(message = "Description is required") 
    private String description; 

    @NotNull(message = "Duration is required") 
    @Min(value = 1, message = "Duration must be at least 1 week")
    private Integer durationWeeks; 

    @NotNull(message = "Status is required") 
    @Enumerated(EnumType.STRING)
    private Status status; 

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Course> courses;

    public enum Status { ACTIVE, INACTIVE }
}