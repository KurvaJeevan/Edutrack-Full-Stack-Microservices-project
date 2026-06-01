package com.cts.edutrack.controller;

import java.util.List;

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
import com.cts.edutrack.model.Question;
import com.cts.edutrack.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/{questionId}")
	public ApiResponse getById(@PathVariable Long questionId) {
		return questionService.getQuestionById(questionId);
	}
	
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping
	public ApiResponse getAll() {
		return questionService.getAllQuestion();
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@PostMapping
	public ApiResponse create(@Valid @RequestBody Question question) {
		return questionService.saveQuestion(question);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@PutMapping("/{questionId}")
	public ApiResponse update(@PathVariable Long questionId, @Valid @RequestBody Question updated) {
		return questionService.updateQuestion(questionId, updated);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@PostMapping("/bulk")
	public ApiResponse bulkCreate(@Valid @RequestBody List<Question> questions) {
		return questionService.saveQuestions(questions);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@DeleteMapping("/{questionId}")
	public ApiResponse delete(@PathVariable Long questionId) {
		return questionService.deleteQuestion(questionId);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
	@GetMapping("/course/{courseId}")
	public ApiResponse getQuestionsByCourseId(@PathVariable Long courseId) {
		return questionService.getQuestionsByCourseId(courseId);
	}
}