package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<StudentReponseDto> listStudents(){

        return studentRepo.findAll()
                .stream()
                .map(StudentMapper::toDto)
                .toList();
    }

    public StudentReponseDto findById(String studentId) {
        UUID id = UUID.fromString(studentId);

         Student foundStudent = studentRepo.findById(id)
                 .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the Database"));

         return StudentMapper.toDto(foundStudent);
    }

    public StudentReponseDto createStudent(StudentRequestDto studentRequestDto) {
        Student newStudent = StudentMapper.toEntity(studentRequestDto);

        Student savedStudent = studentRepo.save(newStudent);

        return StudentMapper.toDto(savedStudent);
    }
}
