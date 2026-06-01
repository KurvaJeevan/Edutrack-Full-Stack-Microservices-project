package com.cts.edutrack.service;

import java.util.Collections;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.model.ApiResponse;
import com.cts.edutrack.model.Program;
import com.cts.edutrack.repository.ProgramRepository;
import com.cts.edutrack.repository.ProgramRepository;

@Service
public class ProgramService {

    private ProgramRepository programRepository;
    private ModelMapper modelMapper;

    public ProgramService(ProgramRepository programRepository, ModelMapper modelMapper) {
        this.programRepository = programRepository;
        this.modelMapper = modelMapper;
    }

    public ApiResponse createProgram(Program program) {
        if (program.getStatus() == null) {
            program.setStatus(Program.Status.ACTIVE); 
        }
        Program saved = programRepository.save(program);
        return new ApiResponse(true, "Program Created Successfully", saved, HttpStatus.CREATED.value(), Collections.emptyList());
    }

    public ApiResponse getAllPrograms() {
        List<Program> programs = programRepository.findAll();
        return new ApiResponse(true, "Programs Fetched Successfully", programs, HttpStatus.OK.value(), Collections.emptyList());
    }

    public ApiResponse getProgramById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Program not found with id: " + id));
        return new ApiResponse(true, "Program Fetched Success", program, HttpStatus.OK.value(), Collections.emptyList());
    }

    public ApiResponse updateProgram(Long id, Program updatedProgram) {
        Program existing = programRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Program not found with id: " + id));

        modelMapper.map(updatedProgram, existing);
        existing.setProgramId(id); 

        Program saved = programRepository.save(existing);
        return new ApiResponse(true, "Program Updated Successfully", saved, HttpStatus.OK.value(), Collections.emptyList());
    }

    public ApiResponse deleteProgram(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Program not found with id: " + id));
        programRepository.delete(program);
        return new ApiResponse(true, "Program Deleted Successfully", null, HttpStatus.OK.value(), Collections.emptyList());
    }
}