package com.cts.edutrack.service;

import org.springframework.stereotype.Service;

import com.cts.edutrack.model.CourseCompletion;
import com.cts.edutrack.model.ModuleProgress;
import com.cts.edutrack.repository.CourseCompletionRepository;
import com.cts.edutrack.repository.CourseRepository;
import com.cts.edutrack.repository.ModuleProgressRepository;
import com.cts.edutrack.repository.ProgramProgressResponse;
import com.cts.edutrack.repository.ProgramRepository;
import com.cts.edutrack.repository.ProgressStatusResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ModuleProgressRepository moduleRepo;
    private final CourseCompletionRepository courseRepo;
    private final CourseRepository courseMainRepo;
    private final ProgramRepository programMainRepo;

    @Transactional
    public void markModuleComplete(Long userId, Long courseId, Long moduleId) {
        if (!moduleRepo.existsByUserIdAndModuleId(userId, moduleId)) {
            moduleRepo.save(new ModuleProgress(userId, courseId, moduleId));
        }
    }

    public ProgressStatusResponse getCourseProgress(Long userId, Long courseId) {
        long completed = moduleRepo.countByUserIdAndCourseId(userId, courseId);
        
        var course = courseMainRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        int totalModules = course.getModules().size();
        boolean isCourseDone = courseRepo.findByUserIdAndCourseId(userId, courseId).isPresent();

        return new ProgressStatusResponse(completed, totalModules, (completed == totalModules), isCourseDone);
    }
    
    
    public boolean isModuleCompleted(Long userId, Long courseId, Long moduleId) {
    	return moduleRepo.existsByUserIdAndModuleId(userId, moduleId);
    	
    }
    

    @Transactional
    public ProgramProgressResponse processAssessmentPass(Long userId, Long courseId, Double score) {
        var course = courseMainRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Long programId = course.getProgram().getProgramId();

        // 1. Record Course Completion (Level 2 Tracker)
        if (courseRepo.findByUserIdAndCourseId(userId, courseId).isEmpty()) {
            courseRepo.save(new CourseCompletion(userId, programId, courseId, score, true));
        }

        // 2. Calculate and return Program Status
        return getProgramProgress(userId, programId);
    }

    public ProgramProgressResponse getProgramProgress(Long userId, Long programId) {
        var program = programMainRepo.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        
        long completedCourses = courseRepo.countByUserIdAndProgramIdAndIsPassed(userId, programId, true);
        int totalCourses = program.getCourses().size();
        
        double percentage = (totalCourses > 0) ? ((double) completedCourses / totalCourses) * 100 : 0;
        boolean isFinished = (completedCourses == totalCourses);

        return new ProgramProgressResponse(programId, completedCourses, totalCourses, percentage, isFinished);
    }
}