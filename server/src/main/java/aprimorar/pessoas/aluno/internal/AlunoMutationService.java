package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.aluno.api.AlunoDeletedEvent;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.internal.repository.AlunoRepository;
import aprimorar.pessoas.responsavel.api.ResponsavelQueryApi;
import aprimorar.pessoas.shared.address.Address;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.BusinessException;
import java.time.Clock;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AlunoQueryService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final AlunoRepository alunoRepo;
    private final AlunoMapper studentMapper;

    private final ResponsavelQueryApi parentQueryApi;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public AlunoMutationService(
        AlunoRepository alunoRepo,
        AlunoMapper studentMapper,
        ResponsavelQueryApi parentQueryApi,
        ApplicationEventPublisher eventPublisher,
        Clock clock
    ) {
        this.alunoRepo = alunoRepo;
        this.parentQueryApi = parentQueryApi;
        this.studentMapper = studentMapper;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Transactional
    public AlunoResponseDTO createAluno(AlunoRequestDTO dto) {
        Aluno student = studentMapper.toEntity(dto);

        if (alunoRepo.existsByCpf(MapperUtils.normalizeCpf(student.getCpf()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(MapperUtils.normalizeEmail(student.getEmail()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este e-mail.");
        }

        parentQueryApi.findResponsavelById(dto.parentId());

        Aluno savedAluno = alunoRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getName().toUpperCase());
        return studentMapper.toResponseDto(savedAluno, clock);
    }

    @Transactional
    public AlunoResponseDTO updateAluno(UUID id, AlunoRequestDTO dto) {
        Aluno student = findStudentOrThrow(id);

        if (GHOST_STUDENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        //TODO: isso aqui talvez pode virar um "existsById"
        parentQueryApi.findResponsavelById(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        student.update(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.email(), dto.school(), dto.parentId(), address);

        log.info("Aluno {} atualizado com sucesso.", student.getName().toUpperCase());
        return studentMapper.toResponseDto(student, clock);
    }

    @Transactional
    public void archiveAluno(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        student.archive();
        log.info("Aluno {} arquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveAluno(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("O registro 'Aluno Removido' não pode ser desarquivado.");
        }
        Aluno student = findStudentOrThrow(studentId);
        student.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void deleteAluno(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);

        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível deletar o registro de sistema 'Aluno Removido'.");
        }

        log.info("Publicando evento de exclusão do aluno {}.", student.getName().toUpperCase());
        eventPublisher.publishEvent(new AlunoDeletedEvent(studentId));

        alunoRepo.delete(student);
        log.info("Aluno {} deletado com sucesso.", student.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return alunoRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }
}
