package com.cts.edutrack.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.EnrollmentRequest;
import com.cts.edutrack.dto.EnrollmentResponse;
import com.cts.edutrack.dto.EnrollmentStatusUpdateRequest;
import com.cts.edutrack.model.Enrollment;
import com.cts.edutrack.model.EnrollmentStatus;
import com.cts.edutrack.service.EnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollments")
@Validated
@RequiredArgsConstructor
public class EnrollmentController {

	private final EnrollmentService enrollmentService;

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@PostMapping
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody EnrollmentRequest request) {
		// Controller-level validations (syntactic)
		if (request.getProgramId() == null || request.getProgramId() <= 0) {
			return badRequest("programId must be > 0");
		}
		if (request.getUserId() == null || request.getUserId() <= 0) {
			return badRequest("userId must be > 0");
		}
		ApiResponse resp = enrollmentService.createEnrollment(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/{enrollmentId}")
	public ResponseEntity<ApiResponse> getById(@PathVariable long enrollmentId) {
		EnrollmentResponse data = enrollmentService.getEnrollmentById(enrollmentId);
		ApiResponse resp = new ApiResponse(true, "Enrollment fetched", data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(resp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/by-Program/{programId}")
	public ResponseEntity<ApiResponse> getByProgram(@PathVariable long programId) {
		List<EnrollmentResponse> data = enrollmentService.getEnrollmentsByProgramId(programId);
		ApiResponse resp = new ApiResponse(true, "Enrollments by Program fetched", data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(resp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/by-student/{userId}")
	public ResponseEntity<ApiResponse> getByStudent(@PathVariable long userId) {
		List<EnrollmentResponse> data = enrollmentService.getEnrollmentsByUserId(userId);
		ApiResponse resp = new ApiResponse(true, "Enrollments by student fetched", data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(resp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
	@PutMapping("/{enrollmentId}/status")
	public ResponseEntity<ApiResponse> updateStatus(@PathVariable long enrollmentId,
			@Valid @RequestBody EnrollmentStatusUpdateRequest request) {

		// Controller-level validation for allowed status strings
		if (!StringUtils.hasText(request.getStatus())) {
			return badRequest("status is required");
		}
		if (!EnrollmentStatus.isValid(request.getStatus())) {
			return badRequest("Invalid status. Allowed: Active, Completed, Dropped");
		}

		EnrollmentResponse data = enrollmentService.updateEnrollmentStatus(enrollmentId, request.getStatus());
		ApiResponse resp = new ApiResponse(true, "Enrollment status updated", data, HttpStatus.OK.value(), null);
		return ResponseEntity.ok(resp);
	}

	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@DeleteMapping("/{enrollmentId}")
	public ResponseEntity<ApiResponse> delete(@PathVariable long enrollmentId) {
		enrollmentService.deleteEnrollment(enrollmentId);
		ApiResponse resp = new ApiResponse(true, "Enrollment deleted", null, HttpStatus.NO_CONTENT.value(), null);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resp);
	}

	private ResponseEntity<ApiResponse> badRequest(String message) {
		ApiResponse resp = new ApiResponse(false, message, null, HttpStatus.BAD_REQUEST.value(), List.of(message));
		return ResponseEntity.badRequest().body(resp);
	}
//	
//	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
//	@GetMapping("/exists")
//	public Boolean hasActiveEnrollment(
//	        @RequestParam Long userId, 
//	        @RequestParam Long programId, 
//	        @RequestParam String status) {
//	    
//	    // Call your service to check if a record exists with these parameters
//	    return enrollmentService.checkEnrollmentExists(userId, programId, status);
//	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/exists")
	public Boolean hasEnrollment(
	        @RequestParam Long userId, 
	        @RequestParam Long programId ) {
	    
	    // Call your service to check if a record exists with these parameters
	    return enrollmentService.isEnrollmentExists(userId, programId);
	}
	 
	 
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse> getAll() {
		List<Enrollment> data = enrollmentService.getAllEnrollments();
		ApiResponse resp = new ApiResponse(true, "All Enrollments are fetched", data, HttpStatus.OK.value(), null);
		System.err.println("okay");
		return ResponseEntity.ok(resp);
	}
	
}
