package aprimorar.financeiro.repasses.service;

import aprimorar.financeiro.api.repasses.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses.RepasseApi;
import aprimorar.financeiro.api.repasses.RepasseResumo;
import aprimorar.financeiro.repasses.domain.RepasseIndividualEntity;
import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualDadosInvalidosException;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualNaoEncontradoException;
import aprimorar.financeiro.repasses.repository.RepasseIndividualRepository;
import aprimorar.financeiro.repasses.repository.specifications.RepasseIndividualSpecifications;
import aprimorar.financeiro.repasses.web.dto.CancelarRepasseIndividualRequest;
import aprimorar.financeiro.repasses.web.dto.RegistrarRepasseIndividualRequest;
import aprimorar.financeiro.repasses.web.dto.RepasseIndividualFiltroRequest;
import aprimorar.financeiro.repasses.web.dto.RepasseIndividualResponse;
import aprimorar.financeiro.repasses.web.dto.RepasseLoteResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepasseIndividualService implements RepasseApi {

    private final RepasseIndividualRepository repasseRepository;

    public RepasseIndividualService(RepasseIndividualRepository repasseRepository) {
        this.repasseRepository = repasseRepository;
    }

    @Override
    @Transactional
    public void criar(CriarRepasseCommand command) {
        if (command == null) {
            throw new RepasseIndividualDadosInvalidosException("Dados do repasse são obrigatórios");
        }
        validarDados(command.atendimentoId(), command.colaboradorId(), command.valor());

        if (repasseRepository.existsByAtendimentoId(command.atendimentoId())) {
            throw new RepasseIndividualDadosInvalidosException(
                "Já existe um repasse para o atendimento informado"
            );
        }

        repasseRepository.save(
            new RepasseIndividualEntity(command.atendimentoId(), command.colaboradorId(), command.valor())
        );
    }

    @Override
    @Transactional
    public void atualizar(AtualizarRepasseCommand command) {
        if (command == null) {
            throw new RepasseIndividualDadosInvalidosException("Dados do repasse são obrigatórios");
        }
        validarDados(command.atendimentoId(), command.colaboradorId(), command.valor());

        RepasseIndividualEntity repasse = repasseRepository
            .findByAtendimentoIdForUpdate(command.atendimentoId())
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);

        repasse.update(command.colaboradorId(), command.valor());
    }

    @Override
    @Transactional
    public void cancelarPorAtendimento(Long atendimentoId) {
        if (atendimentoId == null) {
            throw new RepasseIndividualDadosInvalidosException("ID do atendimento é obrigatório");
        }

        RepasseIndividualEntity repasse = repasseRepository
            .findByAtendimentoIdForUpdate(atendimentoId)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);

        repasse.cancelar();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPendenciaPorColaboradorId(UUID colaboradorId) {
        if (colaboradorId == null) {
            throw new RepasseIndividualDadosInvalidosException("ID do colaborador é obrigatório");
        }

        return repasseRepository.existsByColaboradorIdAndStatus(
            colaboradorId,
            StatusRepasseIndividual.PENDENTE
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, RepasseResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds) {
        if (atendimentoIds == null || atendimentoIds.isEmpty()) {
            return Map.of();
        }

        return repasseRepository.findAllByAtendimentoIdIn(atendimentoIds)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                RepasseIndividualEntity::getAtendimentoId,
                RepasseIndividualService::toResumo
            ));
    }

    @Transactional(readOnly = true)
    public Page<RepasseIndividualResponse> buscarRepasses(
        RepasseIndividualFiltroRequest filtro,
        Pageable pageable
    ) {
        return repasseRepository.findAll(
            RepasseIndividualSpecifications.comFiltros(filtro),
            pageable
        ).map(RepasseIndividualResponse::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RepasseLoteResponse> buscarLotes(UUID colaboradorId, Pageable pageable) {
        return repasseRepository.findLotesPorColaboradorId(
            colaboradorId,
            StatusRepasseIndividual.PAGO,
            pageable
        ).map(RepasseLoteResponse::toDto);
    }

    @Transactional(readOnly = true)
    public RepasseLoteResponse buscarLotePorId(UUID loteId) {
        return repasseRepository.findLotePorId(loteId, StatusRepasseIndividual.PAGO)
            .map(RepasseLoteResponse::toDto)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);
    }

    @Transactional(readOnly = true)
    public RepasseIndividualResponse buscarPorId(Long repasseId) {
        return repasseRepository.findById(repasseId)
            .map(RepasseIndividualResponse::toDto)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);
    }

    @Transactional
    public void registrarRepasse(RegistrarRepasseIndividualRequest request) {
        List<Long> ids = request.repasseIds().stream().toList();
        if (ids.isEmpty()) {
            throw new RepasseIndividualDadosInvalidosException("Informe ao menos um repasse");
        }
        if (ids.size() != ids.stream().distinct().count()) {
            throw new RepasseIndividualDadosInvalidosException("Não informe o mesmo repasse mais de uma vez");
        }

        List<RepasseIndividualEntity> repasses = repasseRepository.findAllByIdInForUpdate(ids);
        if (repasses.size() != ids.size()) {
            throw new RepasseIndividualNaoEncontradoException();
        }
        if (repasses.stream().map(RepasseIndividualEntity::getColaboradorId).distinct().count() > 1) {
            throw new RepasseIndividualDadosInvalidosException(
                "Todos os repasses precisam pertencer ao mesmo colaborador"
            );
        }

        UUID loteId = UUID.randomUUID();
        repasses.forEach(r -> r.registrarRepasse(loteId, request.formaPagamento(), request.comprovanteUrl()));
    }

    @Transactional
    public void cancelarRepasse(CancelarRepasseIndividualRequest request) {
        List<Long> ids = request.repasseIds().stream().toList();
        if (ids.isEmpty()) {
            throw new RepasseIndividualDadosInvalidosException("Informe ao menos um repasse");
        }
        if (ids.size() != ids.stream().distinct().count()) {
            throw new RepasseIndividualDadosInvalidosException("Não informe o mesmo repasse mais de uma vez");
        }

        List<RepasseIndividualEntity> repasses = repasseRepository.findAllByIdInForUpdate(ids);
        if (repasses.size() != ids.size()) {
            throw new RepasseIndividualNaoEncontradoException();
        }

        repasses.forEach(RepasseIndividualEntity::cancelarRepasse);
    }

    private static RepasseResumo toResumo(RepasseIndividualEntity repasse) {
        return new RepasseResumo(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getValor(),
            repasse.getStatus().name(),
            repasse.getDataRepasse(),
            repasse.getFormaPagamento() == null ? null : repasse.getFormaPagamento().name(),
            repasse.getLoteId()
        );
    }

    private static void validarDados(Long atendimentoId, UUID colaboradorId, BigDecimal valor) {
        if (atendimentoId == null) {
            throw new RepasseIndividualDadosInvalidosException("ID do atendimento é obrigatório");
        }
        if (colaboradorId == null) {
            throw new RepasseIndividualDadosInvalidosException("ID do colaborador é obrigatório");
        }
        if (valor == null) {
            throw new RepasseIndividualDadosInvalidosException("Valor do repasse é obrigatório");
        }
    }

}
