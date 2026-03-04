package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.dto.student.UpdateStudentDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
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

    public Page<StudentResponseDTO> listStudents(Pageable pageable, String name, Activity activity) {
        String normalizedName = normalizeNameFilter(name);

        if (normalizedName != null && activity != null) {
            return studentRepo.findByNameContainingIgnoreCaseAndActivity(normalizedName, activity, pageable)
                    .map(studentMapper::toDto);
        }

        if (normalizedName != null) {
            return studentRepo.findByNameContainingIgnoreCase(normalizedName, pageable)
                    .map(studentMapper::toDto);
        }

        if (activity != null) {
            return studentRepo.findByActivity(activity, pageable)
                    .map(studentMapper::toDto);
        }

        return listStudents(pageable);
    }

    public Page<StudentResponseDTO> listActiveStudents(Pageable pageable){
        Page<Student> activeStudentsPage = studentRepo.findAllByActiveTrue(pageable);
        return activeStudentsPage.map(studentMapper::toDto);
    }

    public Page<StudentResponseDTO> listActiveStudents(Pageable pageable, String name, Activity activity) {
        String normalizedName = normalizeNameFilter(name);

        if (normalizedName != null && activity != null) {
            return studentRepo.findAllByActiveTrueAndNameContainingIgnoreCaseAndActivity(normalizedName, activity, pageable)
                    .map(studentMapper::toDto);
        }

        if (normalizedName != null) {
            return studentRepo.findAllByActiveTrueAndNameContainingIgnoreCase(normalizedName, pageable)
                    .map(studentMapper::toDto);
        }

        if (activity != null) {
            return studentRepo.findAllByActiveTrueAndActivity(activity, pageable)
                    .map(studentMapper::toDto);
        }

        return listActiveStudents(pageable);
    }

    public StudentResponseDTO findById(UUID studentId) {
         Student foundStudent = findStudentOrThrow(studentId);
         return studentMapper.toDto(foundStudent);
    }

    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student with name: {}", createStudentDto.name());

        Student newStudent = studentMapper.toEntity(createStudentDto);

        if (createStudentDto.parentId() != null) {
            Parent existingParent = findActiveParentOrThrow(createStudentDto.parentId());
            newStudent.setParent(existingParent);
        } else if (createStudentDto.parent() != null) {
            Parent savedParent = createParent(createStudentDto.parent());
            newStudent.setParent(savedParent);
        } else {
            throw new IllegalArgumentException("Parent is required: provide either parentId or parent");
        }

        Student savedStudent = studentRepo.save(newStudent);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void softDeleteStudent(UUID studentId){
        Student foundStudent = findStudentOrThrow(studentId);
        deactivateIfActive(foundStudent);
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {
        Student foundStudent = findStudentOrThrow(studentId);

        studentMapper.updateFromDto(updateStudentDto, foundStudent);

        if (updateStudentDto.parentId() != null) {
            Parent existingParent = findActiveParentOrThrow(updateStudentDto.parentId());
            foundStudent.setParent(existingParent);
        } else if (updateStudentDto.parent() != null) {
            Parent savedParent = createParent(updateStudentDto.parent());
            foundStudent.setParent(savedParent);
        }

        foundStudent.setUpdatedAt(Instant.now());

        return studentMapper.toDto(foundStudent);
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Parent findActiveParentOrThrow(UUID parentId) {
        return parentRepo.findByIdAndActiveTrue(parentId)
                .orElseThrow(() -> new ParentNotFoundException(parentId));
    }

    private Parent createParent(CreateParentDTO createParentDto) {
        Parent newParent = parentMapper.toEntity(createParentDto);
        return parentRepo.save(newParent);
    }

    private void deactivateIfActive(Student foundStudent) {
        if (Boolean.TRUE.equals(foundStudent.getActive())) {
            foundStudent.setActive(false);
            foundStudent.setUpdatedAt(Instant.now());
        }
    }

    private String normalizeNameFilter(String name) {
        if (name == null) {
            return null;
        }

        String trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
