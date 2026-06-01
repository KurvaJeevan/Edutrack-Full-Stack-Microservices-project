package com.cts.edutrack.exception;

import com.cts.edutrack.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse handleNotFound(NotFoundException ex) {
        return new ApiResponse(false, ex.getMessage(), null, HttpStatus.NOT_FOUND.value(), Collections.emptyList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream().findFirst()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .orElse("Validation error");
        return new ApiResponse(false, msg, null, HttpStatus.BAD_REQUEST.value(), Collections.emptyList());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleAll(Exception ex) {
        return new ApiResponse(false, "Internal Server Error", null,
                HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.singletonList(ex.getMessage()));
    }
}