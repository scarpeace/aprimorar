package com.aprimorar.api.service;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.exception.domain.ParentNotFoundException;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.mapper.StudentMapper;
import com.aprimorar.api.repository.ParentRepository;
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
    private final ParentRepository parentRepo;
    private final StudentMapper studentMapper;
    private final ParentMapper parentMapper;

    public StudentService(StudentRepository studentRepo, ParentRepository parentRepo, StudentMapper studentMapper, ParentMapper parentMapper) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.studentMapper = studentMapper;
        this.parentMapper = parentMapper;
    }

    public Page<StudentResponseDTO> listStudents(Pageable pageable){
        Page<Student> studentPage = studentRepo.findAll(pageable);
        return studentPage.map(studentMapper::toDto);
    }

    public Page<StudentResponseDTO> listActiveStudents(Pageable pageable){
        Page<Student> activeStudentsPage = studentRepo.findAllByActiveTrue(pageable);
        return activeStudentsPage.map(studentMapper::toDto);
    }

    public StudentResponseDTO findById(UUID studentId) {
         Student foundStudent = studentRepo.findById(studentId)
                 .orElseThrow(()-> new StudentNotFoundException(studentId));
         return studentMapper.toDto(foundStudent);
    }

    //TODO Deve ter um jeito melhor de estruturar essa função aqui.Buscar Refatoração.

    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student with name: {}", createStudentDto.name());

        Student newStudent = studentMapper.toEntity(createStudentDto);

        if (createStudentDto.parentId() != null) {
            Parent existingParent = parentRepo.findByIdAndActiveTrue(createStudentDto.parentId())
                    .orElseThrow(() -> new ParentNotFoundException(createStudentDto.parentId()));
            newStudent.setParent(existingParent);
        } else if (createStudentDto.parent() != null) {
            Parent newParent = parentMapper.toEntity(createStudentDto.parent());
            Parent savedParent = parentRepo.save(newParent);
            newStudent.setParent(savedParent);
        } else {
            throw new IllegalArgumentException("Parent is required: provide either parentId or parent");
        }

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
    public StudentResponseDTO updateStudent(UUID studentId, CreateStudentDTO createStudentDto) {
        Student foundStudent = studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        studentMapper.updateFromDto(createStudentDto, foundStudent);

        if (createStudentDto.parentId() != null) {
            Parent existingParent = parentRepo.findByIdAndActiveTrue(createStudentDto.parentId())
                    .orElseThrow(() -> new ParentNotFoundException(createStudentDto.parentId()));
            foundStudent.setParent(existingParent);
        } else if (createStudentDto.parent() != null) {
            Parent newParent = parentMapper.toEntity(createStudentDto.parent());
            Parent savedParent = parentRepo.save(newParent);
            foundStudent.setParent(savedParent);
        }

        foundStudent.setUpdatedAt(Instant.now());

        return studentMapper.toDto(foundStudent);
    }
}
