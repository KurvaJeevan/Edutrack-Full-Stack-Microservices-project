package com.cts.edutrack.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    @NotNull(message = "courseId is required")
    @Positive(message = "courseId must be positive")
    private Long courseId;

    @NotBlank(message = "question is required")
    @Size(max = 500, message = "question must be at most 500 characters")
    private String question;

    @NotBlank(message = "option1 is required")
    @Size(max = 255, message = "option1 must be at most 255 characters")
    private String option1;

    @NotBlank(message = "option2 is required")
    @Size(max = 255, message = "option2 must be at most 255 characters")
    private String option2;

    // If you always require 4 options, keep NotBlank. If not, change to @Size(min=...) logic.
    @NotBlank(message = "option3 is required")
    @Size(max = 255, message = "option3 must be at most 255 characters")
    private String option3;

    @NotBlank(message = "option4 is required")
    @Size(max = 255, message = "option4 must be at most 255 characters")
    private String option4;

    @NotBlank(message = "answer is required")
    @Size(max = 255, message = "answer must be at most 255 characters")
    private String answer;
}