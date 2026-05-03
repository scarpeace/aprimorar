package com.aprimorar.api.domain.student;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.domain.student.repository.StudentSpecifications;
import com.aprimorar.api.shared.MapperUtils;
import com.aprimorar.api.shared.PageDTO;
import com.aprimorar.api.domain.student.dto.StudentSummaryDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
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

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final AddressMapper addressMapper;
    private final EventRepository eventRepo;
    private final Clock clock;

    public StudentService(
        ParentRepository parentRepo,
        StudentRepository studentRepo,
        StudentMapper studentMapper,
        AddressMapper addressMapper,
        EventRepository eventRepo,
        Clock clock
    ) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.addressMapper = addressMapper;
        this.eventRepo = eventRepo;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public StudentSummaryDTO getSummary(UUID studentId, Instant startDate, Instant endDate) {
        if (!studentRepo.existsById(studentId)) {
            throw new StudentNotFoundException("Aluno com o ID informado não encontrado");
        }

        if (startDate == null || endDate == null) {
            long totalEvents = eventRepo.countByStudentId(studentId);
            BigDecimal totalCharged = eventRepo.sumChargedByStudentId(studentId);
            BigDecimal totalPending = eventRepo.sumPendingByStudentId(studentId);
            log.info("Resumo geral gerado para o aluno {}", studentId);
            return new StudentSummaryDTO(totalEvents, totalCharged, totalPending);
        }

        long totalEventsInPeriod = eventRepo.countByStudentIdAndStartDateBetween(studentId, startDate, endDate);
        BigDecimal totalChargedInPeriod = eventRepo.sumChargedByStudentIdInPeriod(studentId, startDate, endDate);
        BigDecimal totalPendingInPeriod = eventRepo.sumPendingByStudentIdInPeriod(studentId, startDate, endDate);

        log.info("Resumo gerado para o aluno {} no período de {} a {}", studentId, startDate, endDate);
        return new StudentSummaryDTO(totalEventsInPeriod, totalChargedInPeriod, totalPendingInPeriod);
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Parent parent = findParentOrThrow(dto.parentId());
        Address address = addressMapper.convertToEntity(dto.address());

        String normalizedContact = MapperUtils.normalizeContact(dto.contact());
        String normalizedEmail = MapperUtils.normalizeEmail(dto.email());
        String normalizedCpf = MapperUtils.normalizeCpf(dto.cpf());

        Student student = new Student(
            dto.name(),
            normalizedContact,
            normalizedEmail,
            dto.birthdate(),
            normalizedCpf,
            dto.school(),
            parent,
            address
        );

        ensureStudentUniqueness(student.getCpf(), student.getEmail());

        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived) {
        Specification<Student> spec = StudentSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(StudentSpecifications.archived());
        } else {
            spec = spec.and(StudentSpecifications.notArchived());
        }

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

    @Transactional(readOnly = true)
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
        Parent parent = findParentOrThrow(dto.parentId());
        Address address = addressMapper.convertToEntity(dto.address());

        String normalizedContact = MapperUtils.normalizeContact(dto.contact());
        String normalizedEmail = MapperUtils.normalizeEmail(dto.email());

        ensureStudentUniquenessForUpdate(normalizedEmail, id);

        student.updateDetails(
            dto.name(),
            normalizedContact,
            normalizedEmail,
            dto.birthdate(),
            dto.school(),
            parent,
            address
        );

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
        eventRepo.reassignStudentEventsToGhost(studentId, GHOST_STUDENT_ID);

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
        if (parentId == null) {
            throw new IllegalArgumentException("Responsável do aluno é obrigatório.");
        }

        return parentRepo
            .findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado."));
    }

    private void ensureStudentUniqueness(String cpf, String email) {
        if (studentRepo.existsByCpf(cpf)) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmail(email)) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

    private void ensureStudentUniquenessForUpdate(String email, UUID studentId) {
        if (studentRepo.existsByEmailAndIdNot(email, studentId)) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }
}
