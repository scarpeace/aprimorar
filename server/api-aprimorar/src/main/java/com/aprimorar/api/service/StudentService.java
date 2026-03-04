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
import com.aprimorar.api.exception.domain.StudentValidationException;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final int MIN_AGE = 10;
    private static final int MAX_AGE = 18;
    private static final ZoneId AGE_CALCULATION_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final String AGE_RANGE_MESSAGE = "A data de nascimento deve resultar em idade entre " + MIN_AGE + " e " + MAX_AGE + " anos";

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

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO createStudentDto) {
        log.info("Creating student with name: {}", createStudentDto.name());
        validateStudentAge(createStudentDto.birthdate());

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
    public void softDeleteStudent(UUID studentId){
        Student foundStudent = findStudentOrThrow(studentId);
        deactivateIfActive(foundStudent);
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {
        Student foundStudent = findStudentOrThrow(studentId);

        if (updateStudentDto.birthdate() != null) {
            validateStudentAge(updateStudentDto.birthdate());
        }

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

    private void validateStudentAge(LocalDate birthdate) {
        LocalDate todayInSaoPaulo = LocalDate.now(AGE_CALCULATION_ZONE);
        int age = Period.between(birthdate, todayInSaoPaulo).getYears();
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new StudentValidationException(AGE_RANGE_MESSAGE);
        }
    }
}
