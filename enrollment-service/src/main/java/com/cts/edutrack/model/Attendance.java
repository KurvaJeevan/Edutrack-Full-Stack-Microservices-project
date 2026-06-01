package com.cts.edutrack.model;
 
import java.time.LocalDate;
import java.time.LocalTime;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Table(name = "attendance",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"userId", "loginDate"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
 
    private Long userId;
 
    private LocalDate loginDate;
 
    private LocalTime loginTime;
}