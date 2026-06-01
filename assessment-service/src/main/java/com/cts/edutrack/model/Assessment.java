package com.cts.edutrack.model;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assessmentId;

	@Positive(message = "courseId must be a positive number")
	private Long courseId;

	@NotNull(message = "type is required")
	@Enumerated(EnumType.STRING)
	private AssessmentType type;

	@Positive(message = "maxMarks must be greater than 0")
	@DecimalMax(value = "100.0", message = "maxMarks cannot exceed 100")
	private double maxMarks;

	@NotNull(message = "dueDate is required")
	@FutureOrPresent(message = "dueDate cannot be in the past")
	private LocalDate dueDate;

	@NotNull(message = "status is required")
	@Enumerated(EnumType.STRING)
	private AssessmentStatus status;
	
}
