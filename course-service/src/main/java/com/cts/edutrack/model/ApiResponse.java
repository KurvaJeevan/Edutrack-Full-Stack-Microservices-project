package com.cts.edutrack.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {
	private boolean success;
	private String message;
	private Object data;
	private Integer statusCode;
	private List<String> errors;
}
