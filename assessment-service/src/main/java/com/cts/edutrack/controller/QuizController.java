package com.cts.edutrack.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.QuizQuestionDTO;
import com.cts.edutrack.model.Question;
import com.cts.edutrack.service.QuestionService;
import com.cts.edutrack.util.QuizMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

	private final QuestionService questionService;

	@GetMapping("/course/{courseId}")
	public ApiResponse getQuizQuestionsByCourse(@PathVariable Long courseId,
			@RequestParam(defaultValue = "10") int size) {

		ApiResponse serviceResp = questionService.getQuestionsByCourseId(courseId);

		if (serviceResp == null || serviceResp.getData() == null) {
			return new ApiResponse(false, "No questions found for courseId: " + courseId, null, 404,
					Collections.emptyList());
		}

		List<Question> allQuestions = (List<Question>) serviceResp.getData();

		if (allQuestions == null || allQuestions.isEmpty()) {
			return new ApiResponse(false, "No questions available for courseId: " + courseId, null, 404,
					Collections.emptyList());
		}

		List<Question> shuffled = new ArrayList<>(allQuestions);
		Collections.shuffle(shuffled);
		List<Question> selected = shuffled.stream().limit(Math.max(0, size)).collect(Collectors.toList());

		Object payload;

		List<QuizQuestionDTO> dtoList = selected.stream().map(QuizMapper::toQuizQuestionDTO)
				.collect(Collectors.toList());
		payload = dtoList;

		return new ApiResponse(true,
				String.format("Returning %d question(s) for courseId: %d", selected.size(), courseId), payload,
				HttpStatus.OK.value(), Collections.emptyList());
	}
}
