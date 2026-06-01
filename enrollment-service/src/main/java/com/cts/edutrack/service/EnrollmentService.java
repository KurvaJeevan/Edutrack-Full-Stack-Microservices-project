package com.cts.edutrack.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.EnrollmentRequest;
import com.cts.edutrack.dto.EnrollmentResponse;
import com.cts.edutrack.exception.BusinessException;
import com.cts.edutrack.exception.ResourceNotFoundException;
import com.cts.edutrack.feignClient.ProgramClient;
import com.cts.edutrack.feignClient.UserClient;
import com.cts.edutrack.model.Enrollment;
import com.cts.edutrack.model.EnrollmentStatus;
import com.cts.edutrack.model.Program;
import com.cts.edutrack.model.User;
import com.cts.edutrack.repository.EnrollmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
	 
	private final EnrollmentRepository enrollmentRepository;
	private final ProgramClient programClient;
	private final UserClient userClient;
	private final ObjectMapper objectMapper;
 
	public ApiResponse createEnrollment(EnrollmentRequest request) {
		Long programId = request.getProgramId();
		Long userId = request.getUserId();
 
		// 1) Check program existence + must be Active
		ApiResponse response = programClient.getProgramById(programId);
		if (!response.isSuccess()) {
			return response;
		}
		Program program = objectMapper.convertValue(response.getData(), Program.class);
		if (Program.Status.ACTIVE != program.getStatus()) {
			throw new BusinessException("Cannot enroll: Program is not Active");
		}
 
		// 2) Check user existence + must be Student
		@Nullable
		ApiResponse response2 = userClient.getUserById(userId).getBody();
		if (!response2.isSuccess()) {
			return response2;
		}
 
		User student = objectMapper.convertValue(response2.getData(), User.class);
		if (!"Student".equalsIgnoreCase(student.getRole())) {
			throw new BusinessException("User is not a Student. Role = " + student.getRole());
		}
 
		// 3) Prevent duplicate/invalid enrollments:
		// - Disallow if an Active enrollment exists
		// - Disallow re-enroll if a Completed enrollment exists (by default)
		boolean hasActive = enrollmentRepository.existsByProgramIdAndUserIdAndStatusIn(programId, userId,
				List.of(EnrollmentStatus.Active.name()));
		if (hasActive) {
			throw new BusinessException("Enrollment already exists with status Active");
		}
 
		boolean hasCompleted = enrollmentRepository.existsByProgramIdAndUserIdAndStatusIn(programId, userId,
				List.of(EnrollmentStatus.Completed.name()));
		if (hasCompleted) {
			throw new BusinessException("Cannot re-enroll: Enrollment already Completed");
		}
 
		// If previous was Dropped, allow re-enrollment (business rule).
		Enrollment enrollment = new Enrollment();
		enrollment.setProgramId(programId);
		enrollment.setUserId(userId);
		enrollment.setEnrolledDate(LocalDateTime.now());
		enrollment.setStatus(EnrollmentStatus.Active.name());
 
		Enrollment saved = enrollmentRepository.save(enrollment);
		EnrollmentResponse enrollmentResponse = toResponse(saved);
		return new ApiResponse(true, "Enrollement Created Successfully", enrollmentResponse, HttpStatus.CREATED.value(),
				Collections.emptyList());
	}
	
	@Transactional(readOnly = true)
	public EnrollmentResponse getEnrollmentById(Long enrollmentId) {
		Enrollment e = enrollmentRepository.findById(enrollmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));
		return toResponse(e);
	}

	@Transactional(readOnly = true)
	public List<EnrollmentResponse> getEnrollmentsByProgramId(Long programId) {
		ApiResponse programCheck = programClient.getProgramById(programId);
		if (!programCheck.isSuccess()) {
			throw new ResourceNotFoundException("Program not found: " + programId);
		}
		return enrollmentRepository.findByProgramId(programId).stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<EnrollmentResponse> getEnrollmentsByUserId(Long userId) {
		ApiResponse userCheck = userClient.getUserById(userId).getBody();
		if (userCheck == null || !userCheck.isSuccess()) {
			throw new ResourceNotFoundException("Student not found: " + userId);
		}
		return enrollmentRepository.findByUserId(userId).stream().map(this::toResponse).toList();
	}

	public EnrollmentResponse updateEnrollmentStatus(Long enrollmentId, String newStatusRaw) {
		Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));

		String newStatus = EnrollmentStatus.normalize(newStatusRaw);
		if (!EnrollmentStatus.isValid(newStatus)) {
			throw new BusinessException("Invalid status. Allowed: Active, Completed, Dropped");
		}

		String current = enrollment.getStatus();

		if (EnrollmentStatus.Completed.name().equalsIgnoreCase(current)
				|| EnrollmentStatus.Dropped.name().equalsIgnoreCase(current)) {
			if (!current.equalsIgnoreCase(newStatus)) {
				throw new BusinessException("Cannot change status from " + current + " to " + newStatus);
			}
		} else if (EnrollmentStatus.Active.name().equalsIgnoreCase(current)) {
			if (!(EnrollmentStatus.Completed.name().equalsIgnoreCase(newStatus)
					|| EnrollmentStatus.Dropped.name().equalsIgnoreCase(newStatus)
					|| EnrollmentStatus.Active.name().equalsIgnoreCase(newStatus))) {
				throw new BusinessException("Invalid transition from Active to " + newStatus);
			}
		}

		enrollment.setStatus(newStatus);
		Enrollment saved = enrollmentRepository.save(enrollment);
		return toResponse(saved);
	}

	public void deleteEnrollment(Long enrollmentId) {
		Enrollment e = enrollmentRepository.findById(enrollmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));

		if (EnrollmentStatus.Completed.name().equalsIgnoreCase(e.getStatus())) {
			throw new BusinessException("Cannot delete a Completed enrollment");
		}

		enrollmentRepository.deleteById(enrollmentId);
	}
	
	public boolean checkEnrollmentExists(Long userId, Long programId, String status) {
	    // Assuming you have a custom method in your Repository
	    return enrollmentRepository.existsByUserIdAndProgramIdAndStatus(userId, programId, status);
	}
	
	public List<Enrollment> getAllEnrollments()
	{
		return enrollmentRepository.findAll();
	}

	private EnrollmentResponse toResponse(Enrollment e) {
		return EnrollmentResponse.builder()
                .enrollmentId(e.getEnrollmentId())
                .programId(e.getProgramId())
				.userId(e.getUserId())
                .enrolledDate(e.getEnrolledDate())
                .status(e.getStatus())
                .build();
	}

	public Boolean isEnrollmentExists(Long userId, Long programId) {
		// TODO Auto-generated method stub
		return enrollmentRepository.existsByUserIdAndProgramId(userId, programId);
		
	}
}