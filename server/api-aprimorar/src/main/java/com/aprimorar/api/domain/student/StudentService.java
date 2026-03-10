package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.time.Clock;
import java.util.UUID;

import com.aprimorar.api.domain.parent.ParentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.parent.dto.CreateParentDTO;
import com.aprimorar.api.domain.student.dto.CreateStudentDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.parent.ParentRepository;

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
        Specification<StudentEntity> filters = Specification.unrestricted();

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
         StudentEntity foundStudentEntity = findAnyStudentOrThrow(studentId);
         return studentMapper.toDto(foundStudentEntity);
     }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student");

        StudentEntity newStudentEntity = studentMapper.toEntity(createStudentDto);

        assignParentReference(newStudentEntity, createStudentDto.parentId(), createStudentDto.parent());

        StudentEntity savedStudentEntity = studentRepo.save(newStudentEntity);
        return studentMapper.toDto(savedStudentEntity);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        StudentEntity foundStudentEntity = findAnyStudentOrThrow(studentId);
        log.info("Archiving studentId={}", studentId);
        archiveIfNotArchived(foundStudentEntity);
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        StudentEntity foundStudentEntity = findAnyStudentOrThrow(studentId);
        log.info("Unarchiving studentId={}", studentId);
        if (foundStudentEntity.getArchivedAt() != null) {
            Instant now = Instant.now(applicationClock);
            foundStudentEntity.setArchivedAt(null);
            foundStudentEntity.setLastReactivatedAt(now);
            foundStudentEntity.setUpdatedAt(now);
        }
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {
        StudentEntity foundStudentEntity = findAnyStudentOrThrow(studentId);
        log.info("Updating studentId={}", studentId);

        studentMapper.updateFromDto(updateStudentDto, foundStudentEntity);

        resolveParentReferenceForUpdate(foundStudentEntity, updateStudentDto.parentId(), updateStudentDto.parent());

        foundStudentEntity.setUpdatedAt(Instant.now(applicationClock));

        return studentMapper.toDto(foundStudentEntity);
    }

    private StudentEntity findAnyStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private ParentEntity findActiveParentOrThrow(UUID parentId) {
        return parentRepo.findByIdAndArchivedAtIsNull(parentId)
                .orElseThrow(() -> new ParentNotFoundException(parentId));
    }

    private ParentEntity createParent(CreateParentDTO createParentDto) {
        ParentEntity newParentEntity = parentMapper.toEntity(createParentDto);
        return parentRepo.save(newParentEntity);
    }

    private void assignParentReference(StudentEntity studentEntity, UUID parentId, CreateParentDTO parentDto) {
        if (parentId != null) {
            ParentEntity existingParentEntity = findActiveParentOrThrow(parentId);
            studentEntity.setParent(existingParentEntity);
            return;
        }

        if (parentDto != null) {
            ParentEntity savedParentEntity = createParent(parentDto);
            studentEntity.setParent(savedParentEntity);
        }
    }

    private void resolveParentReferenceForUpdate(StudentEntity studentEntity, UUID parentId, CreateParentDTO parentDto) {
        if (parentId != null) {
            ParentEntity existingParentEntity = findActiveParentOrThrow(parentId);
            studentEntity.setParent(existingParentEntity);
            return;
        }

        if (parentDto != null && studentEntity.getParent() == null) {
            ParentEntity savedParentEntity = createParent(parentDto);
            studentEntity.setParent(savedParentEntity);
        }
    }

    private void archiveIfNotArchived(StudentEntity foundStudentEntity) {
        if (foundStudentEntity.getArchivedAt() == null) {
            Instant now = Instant.now(applicationClock);
            foundStudentEntity.setArchivedAt(now);
            foundStudentEntity.setUpdatedAt(now);
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
