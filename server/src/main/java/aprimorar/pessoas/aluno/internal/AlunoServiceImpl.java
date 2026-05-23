package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.api.exception.PersonHasPendingFinancialsException;
import aprimorar.pessoas.responsavel.api.ResponsavelReadApi;
import aprimorar.pessoas.shared.address.Address;
import aprimorar.pessoas.aluno.api.AlunoChargeStatusApi;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoCountSummaryDTO;
import aprimorar.pessoas.aluno.api.exception.AlunoNotFoundException;
import aprimorar.pessoas.aluno.internal.repository.AlunoRepository;
import aprimorar.pessoas.aluno.internal.repository.AlunoSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.pessoas.aluno.api.event.AlunoDeletedEvent;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

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
public class AlunoServiceImpl implements AlunoService, AlunoManagementService {

    private static final Logger log = LoggerFactory.getLogger(AlunoServiceImpl.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final AlunoRepository studentRepo;
    private final ResponsavelReadApi parentQueryApi;
    private final AlunoMapper studentMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final AlunoChargeStatusApi studentChargeStatusPort;
    private final Clock clock;

    public AlunoServiceImpl(
        AlunoRepository studentRepo,
        ResponsavelReadApi parentQueryApi,
        AlunoMapper studentMapper,
        ApplicationEventPublisher eventPublisher,
        AlunoChargeStatusApi studentChargeStatusPort,
        Clock clock
    ) {
        this.studentRepo = studentRepo;
        this.parentQueryApi = parentQueryApi;
        this.studentMapper = studentMapper;
        this.eventPublisher = eventPublisher;
        this.studentChargeStatusPort = studentChargeStatusPort;
        this.clock = clock;
    }

    @Transactional
    public AlunoResponseDTO createStudent(AlunoRequestDTO dto) {
        parentQueryApi.findById(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        Aluno student = new Aluno(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.cpf(),
            dto.email(),
            dto.school(),
            dto.parentId(),
            address
        );

        Aluno savedAluno = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getName().toUpperCase());
        return studentMapper.toResponseDto(savedAluno, clock);
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<AlunoResponseDTO> getStudents(Pageable pageable, String search, Boolean archived) {
        Specification<Aluno> spec = AlunoSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(AlunoSpecifications.isArchived());
        }
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(AlunoSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Aluno> studentPage = studentRepo.findAll(spec, pageable);
        Page<AlunoResponseDTO> studentsDtoPage = studentPage.map(student -> studentMapper.toResponseDto(student, clock));

        return new PageDTO<>(studentsDtoPage);
    }

    @Transactional(readOnly = true)
    public AlunoCountSummaryDTO getSummary() {
        Specification<Aluno> notGhost = AlunoSpecifications.isNotGhost();
        long activeStudents = studentRepo.count(notGhost.and(AlunoSpecifications.isNotArchived()));
        long totalStudents = studentRepo.count(notGhost);

        return new AlunoCountSummaryDTO(activeStudents, totalStudents);
    }

    @Transactional(readOnly = true)
    public List<AlunoOptionsDTO> getStudentOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return studentRepo
            .findAll(AlunoSpecifications.isNotArchived(), sort)
            .stream()
            .map(e -> new AlunoOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

   @Transactional(readOnly = true)
    public List<AlunoResponseDTO> getStudentsByParent(UUID parentId) {
        List<Aluno> students = studentRepo.findAllByParentId(parentId);
        return students.stream().map(student -> studentMapper.toResponseDto(student, clock)).toList();
    }

    @Transactional(readOnly = true)
    public boolean hasStudentsLinkedToParent(UUID parentId) {
        return studentRepo.existsByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveStudentsLinkedToParent(UUID parentId) {
        return studentRepo.existsByParentIdAndActiveTrue(parentId);
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO findById(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        return studentMapper.toResponseDto(student, clock);
    }

    @Transactional
    public AlunoResponseDTO updateStudent(AlunoRequestDTO dto, UUID id) {
        if (GHOST_STUDENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        Aluno student = findStudentOrThrow(id);
        parentQueryApi.findById(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        student.updateDetails(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.email(),
            dto.school(),
            dto.parentId(),
            address
        );

        log.info("Aluno {} atualizado com sucesso.", student.getName().toUpperCase());
        return studentMapper.toResponseDto(student, clock);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível arquivar o registro de sistema 'ALUNO ARQUIVADO'.");
        }
        Aluno student = findStudentOrThrow(studentId);
        student.archive();
        log.info("Aluno {} arquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("O registro 'Aluno Removido' não pode ser desarquivado.");
        }
        Aluno student = findStudentOrThrow(studentId);
        student.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível deletar o registro de sistema 'Aluno Removido'.");
        }

        Aluno student = findStudentOrThrow(studentId);

        if (studentChargeStatusPort.hasPendingStudentCharges(studentId)) {
            throw new PersonHasPendingFinancialsException(
                "O aluno possui cobranças pendentes. Quite os valores antes de excluí-lo."
            );
        }

        log.info("Publicando evento de exclusão do aluno {}.", student.getName().toUpperCase());
        eventPublisher.publishEvent(new AlunoDeletedEvent(studentId));

        studentRepo.delete(student);
        log.info("Aluno {} deletado com sucesso.", student.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return studentRepo
            .findById(studentId)
            .orElseThrow(() -> new AlunoNotFoundException("Aluno não encontrado no banco de dados"));
    }
}
