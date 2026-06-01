package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.Course;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.feignClient.CourseClient;
import com.cts.edutrack.feignClient.UserClient;
import com.cts.edutrack.model.Submission;
import com.cts.edutrack.repository.AssessmentRepository;
import com.cts.edutrack.repository.SubmissionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final AssessmentRepository assessmentRepository;
	private final UserClient userClient;
	private final CourseClient courseClient;
	private final ObjectMapper objectMapper;

	public ApiResponse getSubmissionById(Long submissionId) {
		Submission submission = submissionRepository.findById(submissionId)
				.orElseThrow(() -> new NotFoundException("Submission not found: " + submissionId));
		return ok("Submission fetched successfully", submission);
	}

	public ApiResponse getAllSubmission() {
		List<Submission> submissions = submissionRepository.findAll();
		return ok("List of submissions", submissions);
	}

	public ApiResponse saveSubmission(Submission submission) {
		ensureAssessmentExists(submission.getAssessmentId());
		ensureUserExists(submission.getUserId());

		if (submissionRepository.existsByAssessmentIdAndUserId(submission.getAssessmentId(), submission.getUserId())) {
			return badRequest("Submission already exists for this assessment and user");
		}

		Submission saved = submissionRepository.save(submission);
		return created("Submission saved successfully", saved);
	}

	public ApiResponse updateSubmission(Long submissionId, Submission updated) {
		Submission existing = submissionRepository.findById(submissionId)
				.orElseThrow(() -> new NotFoundException("Submission not found: " + submissionId));

		Long targetAssessmentId = (updated.getAssessmentId() != 0) ? updated.getAssessmentId()
				: existing.getAssessmentId();

		Long targetUserId = (updated.getUserId() != 0) ? updated.getUserId() : existing.getUserId();

		ensureAssessmentExists(targetAssessmentId);
		ensureUserExists(targetUserId);

		boolean idsChanged = (targetAssessmentId != existing.getAssessmentId())
				|| (targetUserId != existing.getUserId());

		if (idsChanged && submissionRepository.existsByAssessmentIdAndUserId(targetAssessmentId, targetUserId)) {
			return badRequest("Another submission already exists for this assessment and user");
		}

		existing.setAssessmentId(targetAssessmentId);
		existing.setUserId(targetUserId);

		if (updated.getSubmittedDate() != null) {
			existing.setSubmittedDate(updated.getSubmittedDate());
		}
		existing.setScore(updated.getScore());
		existing.setFeedback(updated.getFeedback());

		Submission saved = submissionRepository.save(existing);
		return ok("Submission updated successfully", saved);
	}

	public ApiResponse deleteSubmission(Long submissionId) {
		Submission existing = submissionRepository.findById(submissionId)
				.orElseThrow(() -> new NotFoundException("Submission not found: " + submissionId));

		submissionRepository.delete(existing);
		return ok("Submission deleted successfully", existing);
	}

	public ApiResponse checkSubmissionByUserIdAndAssessmentId(Long userId, Long assessmentId) {

		Submission submission = submissionRepository.findByAssessmentIdAndUserId(assessmentId, userId)
				.orElseThrow(() -> new NotFoundException("Submission Not Found"));
		return ok("Submission Fetched Successfully", submission);
	}

	public ApiResponse getCourseAverage(Long courseId) {
		Double avg = submissionRepository.findAverageScoreByCourseId(courseId);
		return new ApiResponse(true, "Course Average Calculated", avg == null ? 0.0 : avg, 200,
				Collections.emptyList());
	}

	public ApiResponse getStudentAverage(Long userId) {
		Double avg = submissionRepository.findAverageScoreByUserId(userId);
		return new ApiResponse(true, "Student Overall Average Calculated", avg == null ? 0.0 : avg, 200,
				Collections.emptyList());
	}

	public ApiResponse getProgramAverage(Long programId) {
		List<Long> courseIds = safeCourseIds(programId);

		if (CollectionUtils.isEmpty(courseIds)) {
			// No courses for program, or course-service unreachable -> define as 0.0
			return new ApiResponse(true, "Program Average Calculated", 0.0, 200, Collections.emptyList());
		}

		Double avg = submissionRepository.findAverageScoreByCourseIds(courseIds);
		return new ApiResponse(true, "Program Average Calculated", avg == null ? 0.0 : avg, 200,
				Collections.emptyList());
	}

	public ApiResponse getUserAverageByProgram(Long userId, Long programId) {
		List<Long> courseIds = safeCourseIds(programId);

		if (CollectionUtils.isEmpty(courseIds)) {
			return new ApiResponse(true, "User average calculated for program", 0.0, 200, Collections.emptyList());
		}

		Double avg = submissionRepository.findUserAverageByCourseIds(userId, courseIds);
		return new ApiResponse(true, "User average calculated for program", avg == null ? 0.0 : avg, 200,
				Collections.emptyList());
	}

	public ApiResponse getSubmissionsByUserId(Long userId) {
		// 1. Verify the user actually exists using your Feign client
		ensureUserExists(userId);

		// 2. Fetch all submissions for this user
		List<Submission> submissions = submissionRepository.findByUserId(userId);

		// 3. Return the response
		return ok("Submissions fetched successfully for user: " + userId, submissions);
	}

	// ---------- helpers ----------

	private List<Long> safeCourseIds(Long programId) {
		try {
			ApiResponse response = courseClient.getCoursesByProgram(programId);
			Object data = response.getData();
			if (data == null)
				return Collections.emptyList();

			List<Course> courses = objectMapper.convertValue(data, new TypeReference<List<Course>>() {
			});

			return courses == null ? Collections.emptyList()
					: courses.stream().map(Course::getCourseId).filter(Objects::nonNull).toList();
		} catch (Exception ex) {
			log.warn("Failed to fetch courseIds for program {}", programId, ex);
			return Collections.emptyList();
		}
	}

	private void ensureAssessmentExists(Long assessmentId) {
		if (!assessmentRepository.existsById(assessmentId)) {
			throw new NotFoundException("Assessment not found: " + assessmentId);
		}
	}

	private void ensureUserExists(Long userId) {

		if (!userClient.getUserById(userId).getBody().isSuccess()) {
			throw new NotFoundException("User not found: " + userId);
		}
	}

	private ApiResponse ok(String msg, Object data) {
		return new ApiResponse(true, msg, data, HttpStatus.OK.value(), Collections.emptyList());
	}

	private ApiResponse created(String msg, Object data) {
		return new ApiResponse(true, msg, data, HttpStatus.CREATED.value(), Collections.emptyList());
	}

	private ApiResponse badRequest(String err) {
		return new ApiResponse(false, "Validation failed", null, HttpStatus.BAD_REQUEST.value(), List.of(err));
	}

}