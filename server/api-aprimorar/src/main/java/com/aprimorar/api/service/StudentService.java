package com.aprimorar.api.service;

import java.time.Instant;
import java.time.Clock;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.aprimorar.api.repository.specification.StudentSpecifications;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;
    private final StudentMapper studentMapper;
    private final ParentMapper parentMapper;
    private final Clock applicationClock;

    public StudentService(StudentRepository studentRepo, ParentRepository parentRepo, StudentMapper studentMapper, ParentMapper parentMapper, Clock applicationClock) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.studentMapper = studentMapper;
        this.parentMapper = parentMapper;
        this.applicationClock = applicationClock;
    }

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> listStudents(Pageable pageable, String name, boolean includeArchived) {
        String normalizedName = normalizeNameFilter(name);
        Specification<Student> filters = Specification.unrestricted();

        if (!includeArchived) {
            filters = filters.and(StudentSpecifications.notArchived());
        }

        if (normalizedName != null) {
            filters = filters.and(StudentSpecifications.nameContainsIgnoreCase(normalizedName));
        }

        return studentRepo.findAll(filters, pageable)
                .map(studentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {
         Student foundStudent = findAnyStudentOrThrow(studentId);
         return studentMapper.toDto(foundStudent);
     }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student");

        Student newStudent = studentMapper.toEntity(createStudentDto);

        assignParentReference(newStudent, createStudentDto.parentId(), createStudentDto.parent());

        Student savedStudent = studentRepo.save(newStudent);
        return studentMapper.toDto(savedStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        Student foundStudent = findAnyStudentOrThrow(studentId);
        log.info("Archiving studentId={}", studentId);
        archiveIfNotArchived(foundStudent);
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        Student foundStudent = findAnyStudentOrThrow(studentId);
        log.info("Unarchiving studentId={}", studentId);
        if (foundStudent.getArchivedAt() != null) {
            Instant now = Instant.now(applicationClock);
            foundStudent.setArchivedAt(null);
            foundStudent.setLastReactivatedAt(now);
            foundStudent.setUpdatedAt(now);
        }
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {
        Student foundStudent = findAnyStudentOrThrow(studentId);
        log.info("Updating studentId={}", studentId);

        studentMapper.updateFromDto(updateStudentDto, foundStudent);

        resolveParentReferenceForUpdate(foundStudent, updateStudentDto.parentId(), updateStudentDto.parent());

        foundStudent.setUpdatedAt(Instant.now(applicationClock));

        return studentMapper.toDto(foundStudent);
    }

    private Student findAnyStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Parent findActiveParentOrThrow(UUID parentId) {
        return parentRepo.findByIdAndArchivedAtIsNull(parentId)
                .orElseThrow(() -> new ParentNotFoundException(parentId));
    }

    private Parent createParent(CreateParentDTO createParentDto) {
        Parent newParent = parentMapper.toEntity(createParentDto);
        return parentRepo.save(newParent);
    }

    private void assignParentReference(Student student, UUID parentId, CreateParentDTO parentDto) {
        if (parentId != null) {
            Parent existingParent = findActiveParentOrThrow(parentId);
            student.setParent(existingParent);
            return;
        }

        if (parentDto != null) {
            Parent savedParent = createParent(parentDto);
            student.setParent(savedParent);
        }
    }

    private void resolveParentReferenceForUpdate(Student student, UUID parentId, CreateParentDTO parentDto) {
        if (parentId != null) {
            Parent existingParent = findActiveParentOrThrow(parentId);
            student.setParent(existingParent);
            return;
        }

        if (parentDto != null && student.getParent() == null) {
            Parent savedParent = createParent(parentDto);
            student.setParent(savedParent);
        }
    }

    private void archiveIfNotArchived(Student foundStudent) {
        if (foundStudent.getArchivedAt() == null) {
            Instant now = Instant.now(applicationClock);
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
