package com.cts.edutrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@PreAuthorize("hasAnyRole('ADMIN','STUDENT','INSTRUCTOR')")
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getAttendanceSummary(@PathVariable Long userId) {

		return ResponseEntity.ok(new ApiResponse(true, "Attendance summary fetched successfully",
				attendanceService.getAttendanceSummary(userId), HttpStatus.OK.value(), null));
	}

	@PostMapping("/markAttendance/{userId}")
	public ApiResponse markAttendance(@PathVariable Long userId) {
		return attendanceService.markAttendance(userId);
	}
}