package com.cts.edutrack.exception;

import java.net.BindException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.edutrack.dto.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ApiResponse handleNotFoundException(NotFoundException ex) {
		return new ApiResponse(false, ex.getMessage(), null, HttpStatus.NOT_FOUND.value(),
				Collections.singletonList(ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse handleValidationException(MethodArgumentNotValidException ex) {

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());

		return new ApiResponse(false, "Validation Failed", null, HttpStatus.BAD_REQUEST.value(), errors);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse> handleConstraint(ConstraintViolationException ex) {
		List<String> errors = ex.getConstraintViolations().stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage()).toList();
		ApiResponse resp = new ApiResponse(false, "Validation failed", null, HttpStatus.BAD_REQUEST.value(), errors);
		return ResponseEntity.badRequest().body(resp);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleOthers(Exception ex) {
		ApiResponse resp = new ApiResponse(false, "Internal server error", null,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), List.of(ex.getMessage()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
	}

}