package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.dto.student.UpdateStudentDTO;
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
        Page<Student> studentPage = studentRepo.findByArchivedAtIsNull(pageable);
        return studentPage.map(studentMapper::toDto);
    }

    public Page<StudentResponseDTO> listStudents(Pageable pageable, String name) {
        return listStudents(pageable, name, false);
    }

    public Page<StudentResponseDTO> listStudents(Pageable pageable, String name, Boolean includeArchived) {
        String normalizedName = normalizeNameFilter(name);
        boolean shouldIncludeArchived = Boolean.TRUE.equals(includeArchived);

        if (normalizedName != null) {
            if (shouldIncludeArchived) {
                return studentRepo.findByNameContainingIgnoreCase(normalizedName, pageable)
                        .map(studentMapper::toDto);
            }

            return studentRepo.findByNameContainingIgnoreCaseAndArchivedAtIsNull(normalizedName, pageable)
                    .map(studentMapper::toDto);
        }

        if (shouldIncludeArchived) {
            return studentRepo.findAll(pageable)
                    .map(studentMapper::toDto);
        }

        return listStudents(pageable);
    }

    public StudentResponseDTO findById(UUID studentId) {
         Student foundStudent = findStudentOrThrow(studentId);
         return studentMapper.toDto(foundStudent);
    }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student with name: {}", createStudentDto.name());

        Student newStudent = studentMapper.toEntity(createStudentDto);

        if (createStudentDto.parentId() != null) {
            Parent existingParent = findActiveParentOrThrow(createStudentDto.parentId());
            newStudent.setParent(existingParent);
        } else if (createStudentDto.parent() != null) {
            Parent savedParent = createParent(createStudentDto.parent());
            newStudent.setParent(savedParent);
        }

        Student savedStudent = studentRepo.save(newStudent);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        Student foundStudent = findStudentOrThrow(studentId);
        archiveIfNotArchived(foundStudent);
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        Student foundStudent = findStudentOrThrow(studentId);
        Instant now = Instant.now();

        foundStudent.setArchivedAt(null);
        foundStudent.setLastReactivatedAt(now);
        foundStudent.setUpdatedAt(now);
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

    private void archiveIfNotArchived(Student foundStudent) {
        if (foundStudent.getArchivedAt() == null) {
            Instant now = Instant.now();
            foundStudent.setArchivedAt(now);
            foundStudent.setUpdatedAt(now);
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
