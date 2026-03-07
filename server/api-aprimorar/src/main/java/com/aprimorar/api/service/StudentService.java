package com.aprimorar.api.service;

import java.time.Instant;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
         Student foundStudent = findStudentOrThrow(studentId);
         return studentMapper.toDto(foundStudent);
    }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.debug("Creating student");

        Student newStudent = studentMapper.toEntity(createStudentDto);

        assignParentReference(newStudent, createStudentDto.parentId(), createStudentDto.parent());

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
        if (foundStudent.getArchivedAt() != null) {
            Instant now = Instant.now();
            foundStudent.setArchivedAt(null);
            foundStudent.setLastReactivatedAt(now);
            foundStudent.setUpdatedAt(now);
        }
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {
        Student foundStudent = findStudentOrThrow(studentId);

        studentMapper.updateFromDto(updateStudentDto, foundStudent);

        updateParentReference(foundStudent, updateStudentDto.parentId(), updateStudentDto.parent());

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

    private void updateParentReference(Student student, UUID parentId, CreateParentDTO parentDto) {
        if (parentId != null) {
            Parent existingParent = findActiveParentOrThrow(parentId);
            student.setParent(existingParent);
            return;
        }

        if (parentDto == null) {
            return;
        }

        Parent currentParent = student.getParent();
        if (currentParent == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O aluno precisa ter um responsável vinculado antes de editar os dados do responsável"
            );
        }

        parentMapper.updateFromDto(parentDto, currentParent);
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
