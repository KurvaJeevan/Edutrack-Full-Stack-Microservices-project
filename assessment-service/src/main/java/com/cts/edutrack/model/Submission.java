package com.cts.edutrack.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Submission {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long submissionId;

	@Positive(message = "assessmentId must be a positive number")
	private Long assessmentId;

	@Positive(message = "userId must be a positive number")
	private Long userId;

	@PastOrPresent(message = "submittedDate cannot be in the future")
	private LocalDateTime submittedDate;

	@PositiveOrZero(message = "score cannot be negative")
	private double score;

	@Size(max = 2000, message = "feedback cannot exceed 2000 characters")
	private String feedback;

}
