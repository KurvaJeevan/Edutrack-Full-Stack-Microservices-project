package com.cts.edutrack.dto;

import java.time.LocalDate;

import com.cts.edutrack.model.AssessmentStatus;
import com.cts.edutrack.model.AssessmentType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDto {

	@Positive(message = "courseId must be a positive number")
	private long courseId;

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

	@PrePersist
	public void prePersist() {
		if (type == null)
			type = AssessmentType.QUIZ;
		if (status == null)
			status = AssessmentStatus.ASSIGNED;
	}
}
