package com.cts.edutrack.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.dto.AttendanceSummaryResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.Attendance;
import com.cts.edutrack.repository.AttendanceRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	public ApiResponse markAttendance(Long userId) {

		LocalDate today = LocalDate.now();

		boolean alreadyMarked = attendanceRepository.existsByUserIdAndLoginDate(userId, today);

		if (!alreadyMarked) {

			Attendance attendance = new Attendance();
			attendance.setUserId(userId);
			attendance.setLoginDate(today);
			attendance.setLoginTime(LocalTime.now());

			Attendance savedAttendace = attendanceRepository.save(attendance);
			return new ApiResponse(true, "Attendance summary fetched successfully", savedAttendace,
					HttpStatus.OK.value(), Collections.emptyList());
		}

		return new ApiResponse(false, "Attendance Marked for today", null, HttpStatus.BAD_REQUEST.value(),
				Collections.emptyList());
	}

	public AttendanceSummaryResponse getAttendanceSummary(Long userId) {

		LocalDate minDate = attendanceRepository.findMinLoginDateByUserId(userId);

		if (minDate == null) {
			throw new NotFoundException("No attendance records found for user");
		}

		long totalDays = ChronoUnit.DAYS.between(minDate, LocalDate.now()) + 1;

		long presentDays = attendanceRepository.countDistinctLoginDates(userId);

		long absentDays = totalDays - presentDays;

		return new AttendanceSummaryResponse(userId, totalDays, presentDays, absentDays);
	}
}