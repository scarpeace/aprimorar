package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepo, StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    public Page<StudentReponseDto> listStudents(Pageable pageable){
        Page<Student> studentPage = studentRepo.findAll(pageable);
        return studentPage.map(studentMapper::toDto);
    }

    public Page<StudentReponseDto> listActiveStudents(Pageable pageable){
        Page<Student> activeStudentsPage = studentRepo.findAllByActiveTrue(pageable);
        return activeStudentsPage.map(studentMapper::toDto);
    }

    public StudentReponseDto findById(UUID studentId) {
         Student foundStudent = studentRepo.findById(studentId)
                 .orElseThrow(()-> new StudentNotFoundException(studentId));
         return studentMapper.toDto(foundStudent);
    }

    public StudentReponseDto createStudent(StudentRequestDto studentRequestDto) {
        Student newStudent = studentMapper.toEntity(studentRequestDto);
        Student savedStudent = studentRepo.save(newStudent);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void softDeleteStudent(UUID studentId){
        Student foundStudent = studentRepo.findById(studentId)
                .orElseThrow(()->new StudentNotFoundException(studentId));

        if(Boolean.TRUE.equals(foundStudent.getActive())){
            foundStudent.setActive(false);
            foundStudent.setUpdatedAt(Instant.now());
        }
    }

    @Transactional
    public StudentReponseDto updateStudent(UUID studentId, StudentRequestDto studentRequestDto) {
        Student foundStudent = studentRepo.findById(studentId)
                .orElseThrow(()-> new StudentNotFoundException(studentId));

        studentMapper.updateFromDto(studentRequestDto, foundStudent);
        foundStudent.setUpdatedAt(Instant.now());

        return studentMapper.toDto(foundStudent);
    }
}
