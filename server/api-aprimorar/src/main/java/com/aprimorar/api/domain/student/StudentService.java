package com.aprimorar.api.domain.student;

import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.domain.student.repository.StudentSpecifications;
import com.aprimorar.api.shared.PageDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final EventRepository eventRepo;

    public StudentService(
        ParentRepository parentRepo,
        StudentRepository studentRepo,
        StudentMapper studentMapper,
        EventRepository eventRepo
    ) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.eventRepo = eventRepo;
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Student student = studentMapper.convertToEntity(dto);

        Parent parent = findParentOrThrow(dto.parentId());
        student.setParent(parent);

        ensureStudentUniqueness(student);

        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived) {
        //TODO: Eu acho que dá pra simplificar essa query/especificação do ghost
        Specification<Student> spec = Specification.allOf(
            StudentSpecifications.isNotGhost(),
            Boolean.TRUE.equals(archived) ? StudentSpecifications.archived() : StudentSpecifications.notArchived()
        );

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(StudentSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Student> studentPage = studentRepo.findAll(spec, pageable);
        Page<StudentResponseDTO> studentsDtoPage = studentPage.map(studentMapper::convertToDto);

        return new PageDTO<>(studentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<StudentOptionsDTO> getStudentOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return studentRepo
            .findAll(StudentSpecifications.notArchived(), sort)
            .stream()
            .map(e -> new StudentOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    public PageDTO<StudentResponseDTO> getStudentsByParent(UUID parentId, Pageable pageable) {
        Page<Student> studentPage = studentRepo.findAllByParentId(parentId, pageable);
        Page<StudentResponseDTO> studentsDtoPage = studentPage.map(studentMapper::convertToDto);
        return new PageDTO<>(studentsDtoPage);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return studentMapper.convertToDto(student);
    }

    @Transactional
    public StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id) {
        if (GHOST_STUDENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        Student student = findStudentOrThrow(id);
        Student updatedStudentData = studentMapper.convertToEntity(dto);

        ensureStudentUniquenessForUpdate(updatedStudentData, id);

        student.setName(updatedStudentData.getName());
        student.setContact(updatedStudentData.getContact());
        student.setEmail(updatedStudentData.getEmail());
        student.setBirthdate(updatedStudentData.getBirthdate());
        student.setSchool(updatedStudentData.getSchool());
        student.setAddress(updatedStudentData.getAddress());

        log.info("Aluno {} atualizado com sucesso.", student.getName().toUpperCase());
        return studentMapper.convertToDto(student);
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

    private Parent findParentOrThrow(UUID parentId) {
        return parentRepo
            .findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado."));
    }

    private void ensureStudentUniqueness(Student student) {
        if (studentRepo.existsByCpf(student.getCpf())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmail(student.getEmail())) {
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

    private void ensureParentUniqueness(Parent parent) {
        if (parentRepo.existsByCpfAndIdNot(parent.getCpf(), parent.getId())) {
            throw new ParentAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if (parentRepo.existsByEmailAndIdNot(parent.getEmail(), parent.getId())) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }

    //TODO: Será que vale a pena jogar alguns dos helper methods pra dentro dos methods para melhorar a legibilidade?
    private void ensureParentUniquenessForUpdate(Parent parent, UUID parentId) {
        if (parentRepo.existsByCpfAndIdNot(parent.getCpf(), parentId)) {
            throw new ParentAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if (parentRepo.existsByEmailAndIdNot(parent.getEmail(), parentId)) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }
}
