package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.mapper.AddressMapper;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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

    public String softDeleteStudent(String studentId){
        UUID id = UUID.fromString(studentId);
        Student foundStudent = studentRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the database"));

        if(foundStudent.getActive() == true){
            foundStudent.setActive(false);
            return "Student with id "+ foundStudent.getId() +"is now not active in the database";
        }else{
            return "It wasn't possible to delete Student, check log";
        }
    }

    public StudentReponseDto updateStudent(String studentId, StudentRequestDto studentRequestDto) {
        UUID id = UUID.fromString(studentId);
        Student foundStudent = studentRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found in the Database"));

        Parent updatedParent = ParentMapper.toEntity(studentRequestDto.parent());
        Address updatedAddress = AddressMapper.toEntity(studentRequestDto.address());

        Student updatedStudent = new Student(
                foundStudent.getId(),
                studentRequestDto.name(),
                studentRequestDto.phone(),
                studentRequestDto.birthdate(),
                studentRequestDto.cpf(),
                studentRequestDto.school(),
                studentRequestDto.activity(),
                updatedParent,
                updatedAddress,
                foundStudent.getCreatedAt(),
                Instant.now(),
                true
        );

        studentRepo.save(updatedStudent);

        return StudentMapper.toDto(updatedStudent);
    }
}
