package com.cts.edutrack.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.model.Assessment;
import com.cts.edutrack.service.AssessmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssessmentController {

	
	private final AssessmentService assessmentService;

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/assessment/{assessmentId}")
	public ApiResponse getAssessmentById(@PathVariable Long assessmentId) {
		return assessmentService.getAssessmentById(assessmentId);
	}
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/assessment/courseId/{courseId}")
	public ApiResponse getAssessmentByCourseId(@PathVariable Long courseId) {
		return assessmentService.getAssessmentByCourseId(courseId);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/assessments")
	public ApiResponse getAllAssessment() {
		return assessmentService.getAllAssessment();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@PostMapping("/assessment")
	public ApiResponse saveAssessment(@RequestBody @Valid Assessment assessment) {
		System.err.println(assessment);
		return assessmentService.saveAssessment(assessment);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@DeleteMapping("/assessment/{assessmentId}")
	public ApiResponse deleteAssessment(@PathVariable Long assessmentId) {
		return assessmentService.deleteAssessment(assessmentId);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@PutMapping("/assessment/{assessmentId}")
	public ApiResponse updateAssessment(@PathVariable Long assessmentId, @RequestBody Assessment assessment) {
		return assessmentService.updateAssessment(assessmentId, assessment);
	}
}
