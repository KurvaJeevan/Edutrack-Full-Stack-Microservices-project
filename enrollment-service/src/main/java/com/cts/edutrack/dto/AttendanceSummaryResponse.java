package com.cts.edutrack.dto;
 
import lombok.*;
 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryResponse {
 
    private Long userId;
    private long totalDays;
    private long presentDays;
    private long absentDays;
}