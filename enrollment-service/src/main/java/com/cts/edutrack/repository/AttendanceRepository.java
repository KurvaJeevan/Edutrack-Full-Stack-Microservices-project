package com.cts.edutrack.repository;
 
import java.time.LocalDate;
 
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
 
import com.cts.edutrack.model.Attendance;
 
public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {
 
    boolean existsByUserIdAndLoginDate(Long userId,
                                       LocalDate loginDate);
 
    @Query("SELECT MIN(a.loginDate) FROM Attendance a WHERE a.userId = :userId")
    LocalDate findMinLoginDateByUserId(@Param("userId") Long userId);
 
    @Query("SELECT COUNT(DISTINCT a.loginDate) FROM Attendance a WHERE a.userId = :userId")
    long countDistinctLoginDates(@Param("userId") Long userId);
    
}