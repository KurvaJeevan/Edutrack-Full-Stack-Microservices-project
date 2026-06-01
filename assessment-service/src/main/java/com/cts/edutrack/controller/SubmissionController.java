package com.cts.edutrack.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.model.Submission;
import com.cts.edutrack.service.SubmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class SubmissionController {

	private SubmissionService submissionService;

	public SubmissionController(SubmissionService submissionService) {
		super();
		this.submissionService = submissionService;
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/submission/{submissionId}")
	public ApiResponse getSubmissionById(@PathVariable Long submissionId) {
		return submissionService.getSubmissionById(submissionId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/submissions")
	public ApiResponse getAllSubmission() {
		return submissionService.getAllSubmission();
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@PostMapping("/submission")
	public ApiResponse saveSubmission(@Valid @RequestBody Submission submission) {
		return submissionService.saveSubmission(submission);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@DeleteMapping("/submission/{submissionId}")
	public ApiResponse deleteSubmission(@PathVariable Long submissionId) {
		return submissionService.deleteSubmission(submissionId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
	@PutMapping("/submission/{submissionId}")
	public ApiResponse updateSubmission(@PathVariable Long submissionId, @Valid @RequestBody Submission submission) {
		return submissionService.updateSubmission(submissionId, submission);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("submission/checkSubmission/{userId}/assessment/{assessmentId}")
	public ApiResponse checkSubmissionByUserIdAndAssessmentId(@PathVariable Long userId,@PathVariable Long assessmentId)
	{
		return submissionService.checkSubmissionByUserIdAndAssessmentId(userId,assessmentId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/analysis/course/{courseId}")
	public ApiResponse getCourseAverage(@PathVariable Long courseId) {
		return submissionService.getCourseAverage(courseId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/analysis/program/{programId}")
	public ApiResponse getProgramAverage(@PathVariable Long programId) {
		return submissionService.getProgramAverage(programId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/analysis/student/{userId}")
	public ApiResponse getStudentAverage(@PathVariable Long userId) {
		return submissionService.getStudentAverage(userId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/analysis/student/{userId}/program/{programId}")
	public ApiResponse getUserAverageByProgram(@PathVariable Long userId, @PathVariable Long programId) {
		return submissionService.getUserAverageByProgram(userId, programId);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/analysis/getByUserId/{userId}")
	public ApiResponse getAllSubmissionById(@PathVariable Long userId)
	{
		return submissionService.getSubmissionsByUserId(userId);
	}
}
