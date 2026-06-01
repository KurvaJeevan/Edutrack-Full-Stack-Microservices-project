package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.Assessment;
import com.cts.edutrack.repository.AssessmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentService {

	private final AssessmentRepository assessmentRepository;
	private final ModelMapper modelMapper;

	public ApiResponse getAssessmentById(Long assessmentId) {
		Assessment assessment = assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new NotFoundException("Assessment Not Found"));
		return new ApiResponse(true, "Assessment Fetched Success", assessment , HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse getAllAssessment() {
		List<Assessment> assesmentsList = assessmentRepository.findAll();
		return new ApiResponse(true, "List of Assessments", assesmentsList, HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse saveAssessment(Assessment assessment) {
		if(assessmentRepository.existsByCourseId(assessment.getCourseId())){
			return new ApiResponse(false, "Assessment already exists with Course Id", null, HttpStatus.BAD_REQUEST.value(),
					Collections.emptyList());
		}
		Assessment assessment2 = assessmentRepository.save(assessment);
		return new ApiResponse(true, "Assessment Saved Successfully", assessment2, HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse deleteAssessment(Long assessmentId) {
		Assessment assessment = assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new NotFoundException("Assessment Not Found"));
		assessmentRepository.delete(assessment);
		return new ApiResponse(true, "Assessment Deleted Successfully", assessment, HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse updateAssessment(Long assessmentId, Assessment updated) {

		Assessment existing = assessmentRepository.findById(assessmentId)
				.orElseThrow(() -> new NotFoundException("Assessment not found with id: " + assessmentId));

		if (updated.getCourseId() != existing.getCourseId()) {
			return new ApiResponse(false, "CourseId cannot be changed once the assessment is created", null,
					HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
		}

		modelMapper.map(updated, existing);
		existing.setAssessmentId(assessmentId);

		Assessment saved = assessmentRepository.save(existing);
		return new ApiResponse(true, "Assessment updated successfully", saved, HttpStatus.OK.value(),
				Collections.emptyList());
	}

	public ApiResponse getAssessmentByCourseId(Long courseId) {
		Assessment assessment = assessmentRepository.findByCourseId(courseId);
		return new ApiResponse(true, "Assessment Found By Course Id", assessment, HttpStatus.OK.value(),
				Collections.emptyList());
	}
}
