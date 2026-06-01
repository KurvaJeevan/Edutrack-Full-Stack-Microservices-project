package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.ApiResponse;
import com.cts.edutrack.model.Course;
import com.cts.edutrack.model.Program;
import com.cts.edutrack.repository.CourseRepository;
import com.cts.edutrack.repository.ProgramRepository;
import com.cts.edutrack.repository.CourseRepository;
import com.cts.edutrack.repository.ProgramRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    private final ModelMapper modelMapper;

    public CourseService(CourseRepository courseRepository, ProgramRepository programRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.programRepository = programRepository;
        this.modelMapper = modelMapper;
    }

    // POST: Add Course to a specific Program
    public ApiResponse addCourseToProgram(Long programId, Course course) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new NotFoundException("Program not found with ID: " + programId));

        course.setProgram(program);
        if (course.getStatus() == null) {
            course.setStatus(Course.Status.ACTIVE);
        }
        
        Course saved = courseRepository.save(course);
        return new ApiResponse(true, "Course Saved Successfully", saved, HttpStatus.OK.value(), Collections.emptyList());
    }

    // GET: List all courses for a Program
    public ApiResponse getCoursesByProgram(Long programId) {
        if (!programRepository.existsById(programId)) {
            throw new NotFoundException("Program not found with ID: " + programId);
        }
        List<Course> courses = courseRepository.findByProgram_ProgramId(programId);
        return new ApiResponse(true, "Courses Fetched Successfully", courses, HttpStatus.OK.value(), Collections.emptyList());
    }

    // GET: Specific Course Detail
    public ApiResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + courseId));
        return new ApiResponse(true, "Course Fetched Success", course, HttpStatus.OK.value(), Collections.emptyList());
    }

    // PUT: Update Course
    public ApiResponse updateCourse(Long courseId, Course updatedCourse) {
        Course existing = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + courseId));
        
        Program p = existing.getProgram();
        // Use modelMapper to update fields [consistent with Assessment module]
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(updatedCourse, existing);
        existing.setCourseId(courseId);
        existing.setProgram(p);// Ensure ID doesn't change

        Course saved = courseRepository.save(existing);
        return new ApiResponse(true, "Course Updated Successfully", saved, HttpStatus.OK.value(), Collections.emptyList());
    }

    // DELETE: Remove Course 
    public ApiResponse deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + courseId));
        
        courseRepository.delete(course);
        return new ApiResponse(true, "Course Deleted Successfully", null, HttpStatus.OK.value(), Collections.emptyList());
    }
}