package aprimorar.pessoas.aluno.internal.application;

import aprimorar.atendimentos.api.AtendimentosQueryApi;
import aprimorar.pessoas.aluno.api.AlunoDeletedEvent;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.internal.domain.Aluno;
import aprimorar.pessoas.aluno.internal.infrastructure.persistence.AlunoRepository;
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

    private static final Logger log = LoggerFactory.getLogger(AlunoMutationService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final AlunoRepository alunoRepo;
    private final AlunoMapper alunoMapper;

    private final ResponsavelQueryApi responsavelQueryApi;
    private final AtendimentosQueryApi atendimentosQueryApi;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public AlunoMutationService(
        AlunoRepository alunoRepo,
        AlunoMapper alunoMapper,
        ResponsavelQueryApi responsavelQueryApi,
        AtendimentosQueryApi atendimentosQueryApi,
        ApplicationEventPublisher eventPublisher,
        Clock clock
    ) {
        this.alunoRepo = alunoRepo;
        this.responsavelQueryApi = responsavelQueryApi;
        this.atendimentosQueryApi = atendimentosQueryApi;
        this.alunoMapper = alunoMapper;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Transactional
    public AlunoResponseDTO createAluno(AlunoRequestDTO dto) {
        Aluno aluno = alunoMapper.toEntity(dto);

        if (alunoRepo.existsByCpf(MapperUtils.normalizeCpf(aluno.getCpf()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(MapperUtils.normalizeEmail(aluno.getEmail()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este e-mail.");
        }

        //TODO: isso aqui talvez pode virar um "existsById"
        responsavelQueryApi.findResponsavelById(dto.parentId());

        Aluno savedAluno = alunoRepo.save(aluno);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getName().toUpperCase());
        return alunoMapper.toResponseDto(savedAluno, clock);
    }

    @Transactional
    public AlunoResponseDTO updateAluno(UUID alunoId, AlunoRequestDTO dto) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (GHOST_STUDENT_ID.equals(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        if (alunoRepo.existsByCpfAndIdNot(MapperUtils.normalizeCpf(dto.cpf()), alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno utilizando este CPF.");
        }

        if (alunoRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno utilizando este e-mail.");
        }

        responsavelQueryApi.findResponsavelById(dto.parentId());
        Address address = Address.fromRequest(dto.address());

        aluno.update(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.email(), dto.school(), dto.parentId(), address);

        log.info("Aluno {} atualizado com sucesso.", aluno.getName().toUpperCase());
        return alunoMapper.toResponseDto(aluno, clock);
    }

    @Transactional
    public void archiveAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (atendimentosQueryApi.alunoHasPendingCharges(alunoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "É possivel arquivar um aluno com pendências financeiras.");
        }

        aluno.archive();
        log.info("Aluno {} arquivado com sucesso.", aluno.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        ensureNotGhost(alunoId);

        aluno.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", aluno.getName().toUpperCase());
    }

    @Transactional
    public void deleteAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        ensureNotGhost(alunoId);

        log.info("Publicando evento de exclusão do aluno {}.", aluno.getName().toUpperCase());
        eventPublisher.publishEvent(new AlunoDeletedEvent(alunoId));

        alunoRepo.delete(aluno);
        log.info("Aluno {} deletado com sucesso.", aluno.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Aluno findAlunoOrThrow(UUID alunoId) {
        return alunoRepo
            .findById(alunoId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }

    private void ensureNotGhost(UUID id) {
        if (GHOST_STUDENT_ID.equals(id)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }
    }
}
