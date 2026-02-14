package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Student;
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
        if(studentPage.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No students where found in the database");
        }

        return studentPage.map(studentMapper::toDto);
    }

    public Page<StudentReponseDto> listActiveStudents(Pageable pageable){
        Page<Student> activeStudentsPage = studentRepo.findAllByActiveTrue(pageable);

        if(activeStudentsPage.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No active students where found in the database");
        }

        return activeStudentsPage.map(studentMapper::toDto);
    }

    public StudentReponseDto findById(String studentId) {
        UUID id = UUID.fromString(studentId);

         Student foundStudent = studentRepo.findById(id)
                 .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the Database"));

         return studentMapper.toDto(foundStudent);
    }

    public StudentReponseDto createStudent(StudentRequestDto studentRequestDto) {
        Student newStudent = studentMapper.toEntity(studentRequestDto);

        Student savedStudent = studentRepo.save(newStudent);

        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public String softDeleteStudent(String studentId){
        UUID id = UUID.fromString(studentId);
        Student foundStudent = studentRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the database"));

        if(Boolean.TRUE.equals(foundStudent.getActive())){
            foundStudent.setActive(false);
            foundStudent.setUpdatedAt(Instant.now());
            return "Student with id "+ foundStudent.getId() +" was deactivated in the database";
        }else{
            return "It wasn't possible to deactivate Student, check log";
        }
    }

    @Transactional
    public StudentReponseDto updateStudent(String studentId, StudentRequestDto studentRequestDto) {
        UUID id = UUID.fromString(studentId);
        Student foundStudent = studentRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the Database"));

        studentMapper.updateFromDto(studentRequestDto, foundStudent);
        foundStudent.setUpdatedAt(Instant.now());

        return studentMapper.toDto(foundStudent);
    }
}
