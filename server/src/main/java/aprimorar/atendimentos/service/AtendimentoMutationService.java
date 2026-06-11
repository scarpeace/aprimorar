package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.pessoas.dto.AlunoResponseDTO;
import aprimorar.pessoas.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.events.AlunoQueryApi;
import aprimorar.pessoas.events.ColaboradorQueryApi;
import aprimorar.shared.exception.BusinessException;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoMutationService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AlunoQueryApi alunoQueryApi;
    private final ColaboradorQueryApi colaboradorQueryApi;
    private final Clock clock;

    public AtendimentoMutationService(
        AtendimentoRepository atendimentoRepo,
        AlunoQueryApi alunoQueryApi,
        ColaboradorQueryApi colaboradorQueryApi,
        Clock clock
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoQueryApi = alunoQueryApi;
        this.colaboradorQueryApi = colaboradorQueryApi;
        this.clock = clock;
    }

    @Transactional
    public AtendimentoResponseDTO createAtendimento(AtendimentoRequestDTO dto) {
        AlunoResponseDTO aluno = alunoQueryApi.findAlunoById(dto.alunoId());
        ColaboradorResponseDTO colaborador = colaboradorQueryApi.findColaboradorById(dto.colaboradorId());
        Atendimento atendimento = dto.toEntity(aluno.nome(), colaborador.nome(), clock.instant());

        validateParticipantAvailability(aluno, colaborador, dto.inicio(), dto.duracao(), null);

        Atendimento saved = atendimentoRepo.save(atendimento);
        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitulo().toUpperCase());
        return AtendimentoResponseDTO.toDto(saved);
    }

    @Transactional
    public AtendimentoResponseDTO updateAtendimento(UUID id, AtendimentoRequestDTO dto) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        AlunoResponseDTO aluno = alunoQueryApi.findAlunoById(dto.alunoId());
        ColaboradorResponseDTO colaborador = colaboradorQueryApi.findColaboradorById(dto.colaboradorId());

        validateParticipantAvailability(aluno, colaborador, dto.inicio(), dto.duracao(), atendimento);

        atendimento.update(
            dto.descricao(),
            dto.inicio(),
            dto.duracao(),
            dto.repasse(),
            dto.valor(),
            dto.tipo(),
            aluno.id(),
            aluno.nome(),
            colaborador.id(),
            colaborador.nome(),
            Instant.now(clock)
        );
        log.info("Atendimento {} atualizado com sucesso.", atendimento.getTitulo().toUpperCase());
        return AtendimentoResponseDTO.toDto(atendimento);
    }

    @Transactional
    public void deleteAtendimento(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);

        if (atendimento.getDataPagamentoColaborador() == null || atendimento.getDataCobrancaAluno() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento não pode ser excluído com cobranças ou pagamentos pendentes.");
        }

        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getTitulo().toUpperCase());
    }

    @Transactional
    public AtendimentoResponseDTO alternarCobrancaAluno(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.alternarCobrancaAluno(Instant.now(clock));
        log.info("Status da cobranca do aluno no atendimento {} atualizado.", atendimento.getTitulo());
        return AtendimentoResponseDTO.toDto(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO alternarPagamentoColaborador(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.alternarPagamentoColaborador(Instant.now(clock));
        log.info("Status do pagamento do colaborador no atendimento {} atualizado.", atendimento.getTitulo());
        return AtendimentoResponseDTO.toDto(atendimento);
    }

    private Atendimento findAtendimentoOrThrow(UUID id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
    }

    private void validateParticipantAvailability(
        AlunoResponseDTO aluno,
        ColaboradorResponseDTO colaborador,
        Instant inicio,
        Double duracao,
        Atendimento atendimento
    ) {
        Instant fim = Atendimento.calcularFim(inicio, duracao);

        if (!aluno.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter alunos arquivados");
        }

        if (!colaborador.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter colaboradores arquivados");
        }

        UUID atendimentoId = atendimento != null ? atendimento.getId() : null;

        if (atendimentoRepo.alunoPossuiAtendimentoConflitante(aluno.id(), inicio, fim, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O aluno informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.colaboradorPossuiAtendimentoConflitante(colaborador.id(), inicio, fim, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
