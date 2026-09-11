package aprimorar.atendimentos.individuais.service;

import aprimorar.atendimentos.cobranca_aluno.service.CobrancaAlunoServiceImpl;
import aprimorar.atendimentos.individuais.domain.AtendimentoEntity;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoConflitanteException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoDadosInvalidosException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoNaoEncontradoException;
import aprimorar.atendimentos.individuais.repository.AtendimentoRepository;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoRequest;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.colaborador.api.ColaboradorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AtendimentoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoMutationService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final CobrancaAlunoServiceImpl cobrancaAlunoService;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoMutationService(
        AtendimentoRepository atendimentoRepo,
        CobrancaAlunoServiceImpl cobrancaAlunoService,
        AlunoService alunoService,
        ColaboradorService colaboradorService
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.cobrancaAlunoService = cobrancaAlunoService;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional
    public Long agendar(AtendimentoRequest dto) {
        AtendimentoEntity atendimento = dto.toEntity();

        validarDisponibilidadeDosParticipantes(
            dto.alunoId(),
            dto.colaboradorId(),
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            null
        );

        AtendimentoEntity saved = atendimentoRepo.save(atendimento);

        cobrancaAlunoService.criarParaAtendimento(
            saved.getId(),
            saved.getAlunoId(),
            dto.pagamentoAluno()
        );

        log.info("Atendimento {} cadastrado com sucesso.", saved.getId());
        return saved.getId();
    }

    @Transactional
    public void update(Long id, AtendimentoRequest dto) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);

        if (!alunoService.existsById(dto.alunoId())) {
            throw new AtendimentoDadosInvalidosException("Aluno informado não encontrado");
        }
        if (!colaboradorService.existsById(dto.colaboradorId())) {
            throw new AtendimentoDadosInvalidosException("Colaborador informado não encontrado");
        }

        validarDisponibilidadeDosParticipantes(
            dto.alunoId(),
            dto.colaboradorId(),
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            id
        );

        atendimento.update(
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            dto.tipo(),
            dto.alunoId(),
            dto.colaboradorId(),
            dto.pagamentoAluno(),
            dto.repasseColaborador()
        );

        atendimentoRepo.save(atendimento);
        cobrancaAlunoService.atualizarPorAtendimento(
            atendimento.getId(),
            dto.alunoId(),
            dto.pagamentoAluno()
        );
        log.info("Atendimento {} atualizado com sucesso.", atendimento.getId());
    }

    @Transactional
    public void excluir(Long id) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);

        cobrancaAlunoService.excluirPorAtendimento(atendimento.getId());
        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getId());
    }

    private AtendimentoEntity findAtendimentoOrThrow(Long id) {
        return atendimentoRepo.findById(id).orElseThrow(AtendimentoNaoEncontradoException::new);
    }

    private void validarDisponibilidadeDosParticipantes(
        UUID alunoId,
        UUID colaboradorId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long ignoredAtendimentoId
    ) {
        if (atendimentoRepo.alunoPossuiAtendimentoConflitante(
            alunoId,
            inicio,
            fim,
            ignoredAtendimentoId
        )) {
            throw new AtendimentoConflitanteException("O aluno informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.colaboradorPossuiAtendimentoConflitante(
            colaboradorId,
            inicio,
            fim,
            ignoredAtendimentoId
        )) {
            throw new AtendimentoConflitanteException("O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
