package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.parent.ParentRules;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.StudentSummaryDTO;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.domain.student.repository.StudentSpecifications;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final ParentMapper parentMapper;
    private final EventRepository eventRepo;

    public StudentService(
        ParentRepository parentRepo,
        StudentRepository studentRepo,
        StudentMapper studentMapper,
        ParentMapper parentMapper,
        EventRepository eventRepo
    ) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.parentMapper = parentMapper;
        this.eventRepo = eventRepo;
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived) {
        Specification<Student> spec = Specification.allOf(
            StudentSpecifications.isNotGhost(),
            Boolean.TRUE.equals(archived) ? StudentSpecifications.archived() : StudentSpecifications.notArchived()
        );

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(StudentSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Student> page = studentRepo.findAll(spec, pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(studentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryDTO> getStudentSummary() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return studentRepo
            .findAll(StudentSpecifications.notArchived(), sort)
            .stream()
            .map(e -> new StudentSummaryDTO(e.getId(), e.getName()))
            .toList();
    }

    public List<StudentResponseDTO> getStudentsByParent(UUID parentId) {
        List<Student> studentParentsList = studentRepo.findAllByParentId(parentId);
        return studentParentsList.stream().map(studentMapper::convertToDto).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return studentMapper.convertToDto(student);
    }

    /* ----- Command Methods ----- */
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) {
        Student student = studentMapper.convertToEntity(studentRequestDto);

        ensureParentUniqueness(student.getParent());
        ensureStudentUniqueness(student);

        student.setParent(student.getParent());

        // TODO: mover essas regras de validação pra dentro da entidade
        StudentRules.validate(student);

        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

   @Transactional
public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO dto) {
    if (GHOST_STUDENT_ID.equals(id)) {
        throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
    }

    Student entity = findStudentOrThrow(id);
    Student updatedData = studentMapper.convertToEntity(dto);

    ensureStudentUniquenessForUpdate(updatedData, id);
    StudentRules.validate(updatedData);

    Parent currentParent = entity.getParent();
    Parent parentData = updatedData.getParent();

    ensureParentUniquenessForUpdate(parentData, currentParent.getId());
    ParentRules.validate(parentData);

    currentParent.setName(parentData.getName());
    currentParent.setContact(parentData.getContact());
    currentParent.setEmail(parentData.getEmail());
    currentParent.setCpf(parentData.getCpf());

    entity.setName(updatedData.getName());
    entity.setContact(updatedData.getContact());
    entity.setEmail(updatedData.getEmail());
    entity.setBirthdate(updatedData.getBirthdate());
    entity.setCpf(updatedData.getCpf());
    entity.setSchool(updatedData.getSchool());
    entity.setAddress(updatedData.getAddress());

    log.info("Aluno {} atualizado com sucesso.", entity.getName().toUpperCase());
    return studentMapper.convertToDto(entity);
}


    @Transactional
    public void archiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível arquivar o registro de sistema 'ALUNO ARQUIVADO'.");
        }
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(Instant.now());
        log.info("Aluno {} arquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("O registro 'Aluno Removido' não pode ser desarquivado.");
        }
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(null);
        log.info("Aluno {} desarquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível deletar o registro de sistema 'Aluno Removido'.");
        }

        Student student = findStudentOrThrow(studentId);

        // Reatribui eventos para o aluno fantasma antes de deletar
        log.info("Reatribuindo eventos do aluno {} para o Ghost Student.", student.getName().toUpperCase());
        eventRepo.reassignEventsToGhost(studentId, GHOST_STUDENT_ID);

        studentRepo.delete(student);
        log.info("Aluno {} deletado com sucesso.", student.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo
            .findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado no banco de dados"));
    }

    private void ensureStudentUniqueness(Student student) {
        if (studentRepo.existsByCpfAndIdNot(student.getCpf(), student.getId())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmailAndIdNot(student.getEmail(), student.getId())) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

    private void ensureParentUniqueness(Parent parent) {
        if (parentRepo.existsByCpfAndIdNot(parent.getCpf(), parent.getId())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (parentRepo.existsByEmailAndIdNot(parent.getEmail(), parent.getId())) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }


    private void ensureParentUniquenessForUpdate(Parent parent, UUID parentId) {
        if (parentRepo.existsByCpfAndIdNot(parent.getCpf(),parentId)) {
        throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
    }

    if (parentRepo.existsByEmailAndIdNot(parent.getEmail(),parentId)) {
        throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
    }
    }



    private void ensureStudentUniquenessForUpdate(Student student, UUID studentId) {
        if (studentRepo.existsByCpfAndIdNot(student.getCpf(), studentId)) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmailAndIdNot(student.getEmail(), studentId)) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }
}
