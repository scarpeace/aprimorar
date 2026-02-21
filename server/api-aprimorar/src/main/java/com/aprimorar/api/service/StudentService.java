package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentResponseDto;
import com.aprimorar.api.controller.dto.CreateStudentDto;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepo, StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    public Page<StudentResponseDto> listStudents(Pageable pageable){
        Page<Student> studentPage = studentRepo.findAll(pageable);
        return studentPage.map(studentMapper::toDto);
    }

    public Page<StudentResponseDto> listActiveStudents(Pageable pageable){
        Page<Student> activeStudentsPage = studentRepo.findAllByActiveTrue(pageable);
        return activeStudentsPage.map(studentMapper::toDto);
    }

    public StudentResponseDto findById(UUID studentId) {
         Student foundStudent = studentRepo.findById(studentId)
                 .orElseThrow(()-> new StudentNotFoundException(studentId));
         return studentMapper.toDto(foundStudent);
    }

    public StudentResponseDto createStudent(CreateStudentDto createStudentDto) {
        log.info("Creating student with name: {}", createStudentDto.name());
        Student newStudent = studentMapper.toEntity(createStudentDto);
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
    public StudentResponseDto updateStudent(UUID studentId, CreateStudentDto createStudentDto) {
        Student foundStudent = studentRepo.findById(studentId)
                .orElseThrow(()-> new StudentNotFoundException(studentId));

        studentMapper.updateFromDto(createStudentDto, foundStudent);
        foundStudent.setUpdatedAt(Instant.now());

        return studentMapper.toDto(foundStudent);
    }
}
