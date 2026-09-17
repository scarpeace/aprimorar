package aprimorar.atendimentos.individuais.atendimentos.service;

import aprimorar.atendimentos.individuais.atendimentos.domain.AtendimentoIndividualEntity;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.atendimentos.individuais.atendimentos.repository.AtendimentoIndividualRepository;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.CobrancaIndividualEntity;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualNaoEncontradoException;
import aprimorar.financeiro.cobrancas.repository.CobrancaIndividualRepository;
import aprimorar.financeiro.repasses.domain.RepasseIndividualEntity;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualNaoEncontradoException;
import aprimorar.financeiro.repasses.repository.RepasseIndividualRepository;
import aprimorar.pessoas.api.AlunoService;
import aprimorar.pessoas.api.ColaboradorService;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoIndividualService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoIndividualService.class);
    private final AtendimentoIndividualRepository atendimentoRepository;
    private final CobrancaIndividualRepository cobrancaRepository;
    private final RepasseIndividualRepository repasseRepository;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoIndividualService(
        AtendimentoIndividualRepository atendimentoRepository,
        CobrancaIndividualRepository cobrancaRepository,
        RepasseIndividualRepository repasseRepository,
        AlunoService alunoService,
        ColaboradorService colaboradorService
    ) {
        this.atendimentoRepository = atendimentoRepository;
        this.cobrancaRepository = cobrancaRepository;
        this.repasseRepository = repasseRepository;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional
    public Long agendar(AtendimentoIndividualRequest request) {

        validarParticipantes(request.alunoId(), request.colaboradorId());

        validarDisponibilidade(
            request.alunoId(),
            request.colaboradorId(),
            request.dataHoraInicio(),
            request.dataHoraFim(),
            null
        );

        AtendimentoIndividualEntity atendimento = atendimentoRepository.save(request.toEntity());

        CobrancaIndividualEntity cobranca = new CobrancaIndividualEntity(
            atendimento.getId(), atendimento.getAlunoId(), request.valorCobranca()
        );
        RepasseIndividualEntity repasse = new RepasseIndividualEntity(
            atendimento.getId(), atendimento.getColaboradorId(), request.valorRepasse()
        );
        validarValores(cobranca.getValor(), repasse.getValor());
        cobrancaRepository.save(cobranca);
        repasseRepository.save(repasse);

        log.info("Atendimento individual {} cadastrado com cobrança e repasse pendentes.", atendimento.getId());
        return atendimento.getId();
    }

    @Transactional
    public void update(Long id, AtendimentoIndividualRequest request) {
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        validarParticipantes(request.alunoId(), request.colaboradorId());
        validarDisponibilidade(
            request.alunoId(), request.colaboradorId(), request.dataHoraInicio(), request.dataHoraFim(), id
        );

        CobrancaIndividualEntity cobranca = cobrancaRepository.findByAtendimentoIdForUpdate(id)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);
        RepasseIndividualEntity repasse = repasseRepository.findByAtendimentoIdForUpdate(id)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);

        cobranca.update(request.alunoId(), request.valorCobranca());
        repasse.update(request.colaboradorId(), request.valorRepasse());

        validarValores(cobranca.getValor(), repasse.getValor());

        atendimento.update(
            request.dataHoraInicio(), request.dataHoraFim(), request.tipo(), request.alunoId(), request.colaboradorId()
        );

        log.info("Atendimento individual {} atualizado.", id);
    }

    @Transactional
    public void excluir(Long id) {
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        CobrancaIndividualEntity cobranca = cobrancaRepository.findByAtendimentoIdForUpdate(id)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);
        RepasseIndividualEntity repasse = repasseRepository.findByAtendimentoIdForUpdate(id)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);

        if (cobranca.getStatus() == StatusCobrancaIndividual.PAGO) {
            throw new CobrancaIndividualDadosInvalidosException(
                "Não é possível excluir um atendimento com cobrança paga"
            );
        }
        if (repasse.getStatus() == StatusRepasseIndividual.PAGO) {
            throw new RepasseIndividualDadosInvalidosException(
                "Não é possível excluir um atendimento com repasse pago"
            );
        }

        cobrancaRepository.delete(cobranca);
        repasseRepository.delete(repasse);
        atendimentoRepository.delete(atendimento);
        log.info("Atendimento individual {} excluído.", id);
    }


    private AtendimentoIndividualEntity findAtendimentoOrThrow(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(AtendimentoIndividualNaoEncontradoException::new);
    }

    private void validarParticipantes(UUID alunoId, UUID colaboradorId) {
        if (!alunoService.existsById(alunoId)) {
            throw new AtendimentoIndividualDadosInvalidosException("Aluno informado não encontrado");
        }
        if (!colaboradorService.existsById(colaboradorId)) {
            throw new AtendimentoIndividualDadosInvalidosException("Colaborador informado não encontrado");
        }
    }

    private void validarValores(BigDecimal valorCobranca, BigDecimal valorRepasse) {
        if (valorCobranca.compareTo(valorRepasse) < 0) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "O valor da cobrança não pode ser menor que o repasse"
            );
        }
    }

    private void validarDisponibilidade(
        UUID alunoId, UUID colaboradorId, LocalDateTime inicio, LocalDateTime fim, Long ignoredAtendimentoId
    ) {
        if (atendimentoRepository.alunoPossuiAtendimentoConflitante(
            alunoId, inicio, fim, ignoredAtendimentoId
        )) {
            throw new AtendimentoIndividualConflitanteException(
                "O aluno informado ja possui um atendimento no intervalo"
            );
        }
        if (atendimentoRepository.colaboradorPossuiAtendimentoConflitante(
            colaboradorId, inicio, fim, ignoredAtendimentoId
        )) {
            throw new AtendimentoIndividualConflitanteException(
                "O colaborador informado ja possui um atendimento no intervalo"
            );
        }
    }
}
