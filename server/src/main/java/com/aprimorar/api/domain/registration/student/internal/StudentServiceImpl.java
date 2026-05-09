package com.aprimorar.api.domain.registration.student.internal;

import com.aprimorar.api.domain.registration.address.api.Address;
import com.aprimorar.api.domain.registration.parent.api.ParentService;
import com.aprimorar.api.domain.registration.parent.api.dto.ParentResponseDTO;
import com.aprimorar.api.domain.registration.parent.internal.Parent;
import com.aprimorar.api.domain.registration.parent.internal.repository.ParentRepository;
import com.aprimorar.api.domain.registration.student.api.StudentService;
import com.aprimorar.api.domain.registration.student.api.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentRequestDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentResponseDTO;
import com.aprimorar.api.domain.registration.student.api.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.registration.student.api.exception.StudentNotFoundException;
import com.aprimorar.api.domain.registration.student.internal.repository.StudentRepository;
import com.aprimorar.api.domain.registration.student.internal.repository.StudentSpecifications;
import com.aprimorar.api.shared.MapperUtils;
import com.aprimorar.api.shared.PageDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentSummaryDTO;
import com.aprimorar.api.domain.registration.student.api.event.StudentDeletedEvent;
import com.aprimorar.api.domain.event.api.EventService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;
    private final ParentService parentService;
    private final EventService eventService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public StudentServiceImpl(
        StudentRepository studentRepo,
        ParentRepository parentRepo,
        ParentService parentService,
        EventService eventService,
        ApplicationEventPublisher eventPublisher,
        Clock clock
    ) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.parentService = parentService;
        this.eventService = eventService;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public StudentSummaryDTO getSummary(UUID studentId, Instant startDate, Instant endDate) {
        if (!studentRepo.existsById(studentId)) {
            throw new StudentNotFoundException("Aluno com o ID informado não encontrado");
        }

        if (startDate == null || endDate == null) {
            long totalEvents = eventService.countByStudentId(studentId);
            BigDecimal totalCharged = eventService.sumChargedByStudentId(studentId);
            BigDecimal totalPending = eventService.sumPendingByStudentId(studentId);
            log.info("Resumo geral gerado para o aluno {}", studentId);
            return new StudentSummaryDTO(totalEvents, totalCharged, totalPending);
        }

        long totalEventsInPeriod = eventService.countByStudentIdAndStartDateBetween(studentId, startDate, endDate);
        BigDecimal totalChargedInPeriod = eventService.sumChargedByStudentIdInPeriod(studentId, startDate, endDate);
        BigDecimal totalPendingInPeriod = eventService.sumPendingByStudentIdInPeriod(studentId, startDate, endDate);

        log.info("Resumo gerado para o aluno {} no período de {} a {}", studentId, startDate, endDate);
        return new StudentSummaryDTO(totalEventsInPeriod, totalChargedInPeriod, totalPendingInPeriod);
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Parent parent = findParentOrThrow(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        Student student = new Student(
            dto.name(),
            dto.contact(),
            dto.email(),
            dto.birthdate(),
            dto.cpf(),
            dto.school(),
            parent,
            address
        );

        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return savedStudent.toResponseDto(clock);
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
        Page<StudentResponseDTO> studentsDtoPage = studentPage.map(student -> student.toResponseDto(clock));

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

//    @Transactional(readOnly = true)
//    public PageDTO<StudentResponseDTO> getStudentsByParent(UUID parentId, Pageable pageable) {
//        Page<Student> studentPage = studentRepo.findAllByParentId(parentId, pageable);
//        Map<UUID, ParentResponseDTO> parentsById = loadParentsById(studentPage.getContent());
//        Page<StudentResponseDTO> studentsDtoPage = studentPage.map(student -> student.toResponseDto(clock, parentsById.get(student.getParent().getId())));
//        return new PageDTO<>(studentsDtoPage);
//    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {
        Student student = findStudentOrThrow(studentId);

        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return student.toResponseDto(clock);
    }

//    @Transactional(readOnly = true)
//    public Map<UUID, StudentResponseDTO> findByIds(Collection<UUID> studentIds) {
//        if (studentIds == null || studentIds.isEmpty()) {
//            return Map.of();
//        }
//        List<Student> students = studentRepo.findAllById(studentIds);
//        Map<UUID, ParentResponseDTO> parentsById = loadParentsById(students);
//        return students.stream()
//            .collect(Collectors.toMap(Student::getId, student -> student.toResponseDto(clock, parentsById.get(student.getParent().getId()))));
//    }

//    @Transactional(readOnly = true)
//    public boolean existsById(UUID id) {
//        return studentRepo.existsById(id);
//    }

//    @Transactional(readOnly = true)
//    public boolean hasActiveLinkedStudents(UUID parentId) {
//        return studentRepo.existsByParentIdAndArchivedAtIsNull(parentId);
//    }

    @Transactional
    public StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id) {
        if (GHOST_STUDENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        Student student = findStudentOrThrow(id);
        Parent parent = findParentOrThrow(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        student.updateDetails(
            dto.name(),
            dto.contact(),
            dto.email(),
            dto.birthdate(),
            dto.school(),
            parent,
            address
        );

        log.info("Aluno {} atualizado com sucesso.", student.getName().toUpperCase());
        return student.toResponseDto(clock);
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

        log.info("Publicando evento de exclusão do aluno {}.", student.getName().toUpperCase());
        eventPublisher.publishEvent(new StudentDeletedEvent(studentId));

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
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));
    }

//    private Map<UUID, ParentResponseDTO> loadParentsById(List<Student> students) {
//        List<UUID> parentIds = students.stream()
//            .map(s -> s.getParent().getId())
//            .distinct()
//            .toList();
//        return parentService.findByIds(parentIds);
//    }

//    private ParentResponseDTO findParentOrThrow(UUID parentId) {
//        if (parentId == null) {
//            throw new IllegalArgumentException("Responsável do aluno é obrigatório.");
//        }
//
//        return parentService.findById(parentId);
//    }

//    private void ensureStudentUniqueness(String cpf, String email) {
//        if (studentRepo.existsByCpf(cpf)) {
//            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
//        }
//
//        if (studentRepo.existsByEmail(email)) {
//            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
//        }
//    }
//
//    private void ensureStudentUniquenessForUpdate(String email, UUID studentId) {
//        if (studentRepo.existsByEmailAndIdNot(email, studentId)) {
//            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
//        }
//    }
}
