package aprimorar.financeiro.cobrancas.service;

import aprimorar.financeiro.api.cobrancas.AtualizarCobrancaCommand;
import aprimorar.financeiro.api.cobrancas.CobrancaApi;
import aprimorar.financeiro.api.cobrancas.CobrancaResumo;
import aprimorar.financeiro.api.cobrancas.CriarCobrancaCommand;
import aprimorar.financeiro.cobrancas.domain.CobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualNaoEncontradoException;
import aprimorar.financeiro.cobrancas.repository.CobrancaIndividualRepository;
import aprimorar.financeiro.cobrancas.repository.CobrancaIndividualSpecifications;
import aprimorar.financeiro.cobrancas.web.dto.CancelarCobrancasIndividualRequest;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaIndividualFiltroRequest;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaIndividualResponse;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaLoteDetalheResponse;

import aprimorar.financeiro.cobrancas.web.dto.CobrancaLoteResponse;
import aprimorar.financeiro.cobrancas.web.dto.RegistrarPagamentoIndividualRequest;
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
public class CobrancaIndividualService implements CobrancaApi {

    private final CobrancaIndividualRepository cobrancaRepository;

    public CobrancaIndividualService(CobrancaIndividualRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Override
    @Transactional
    public void criar(CriarCobrancaCommand command) {
        if (command == null) {
            throw new CobrancaIndividualDadosInvalidosException("Dados da cobrança são obrigatórios");
        }
        validarDados(command.atendimentoId(), command.alunoId(), command.valor());

        if (cobrancaRepository.existsByAtendimentoId(command.atendimentoId())) {
            throw new CobrancaIndividualDadosInvalidosException(
                "Já existe uma cobrança para o atendimento informado"
            );
        }

        cobrancaRepository.save(
            new CobrancaIndividual(command.atendimentoId(), command.alunoId(), command.valor())
        );
    }

    @Override
    @Transactional
    public void atualizar(AtualizarCobrancaCommand command) {
        if (command == null) {
            throw new CobrancaIndividualDadosInvalidosException("Dados da cobrança são obrigatórios");
        }
        validarDados(command.atendimentoId(), command.alunoId(), command.valor());

        CobrancaIndividual cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(command.atendimentoId())
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);

        cobranca.update(command.alunoId(), command.valor());
    }

    @Override
    @Transactional
    public void cancelarPorAtendimento(Long atendimentoId) {

        if (atendimentoId == null) {
            throw new CobrancaIndividualDadosInvalidosException("ID do atendimento é obrigatório");
        }

        CobrancaIndividual cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(atendimentoId)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);

        cobranca.cancelar();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPendenciaPorAlunoId(UUID alunoId) {
        if (alunoId == null) {
            throw new CobrancaIndividualDadosInvalidosException("ID do aluno é obrigatório");
        }

        return cobrancaRepository.existsByAlunoIdAndStatus(
            alunoId,
            StatusCobrancaIndividual.PENDENTE
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, CobrancaResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds) {
        if (atendimentoIds == null || atendimentoIds.isEmpty()) {
            return Map.of();
        }

        return cobrancaRepository.findAllByAtendimentoIdIn(atendimentoIds)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                CobrancaIndividual::getAtendimentoId,
                CobrancaIndividualService::toResumo
            ));
    }

    @Transactional(readOnly = true)
    public Page<CobrancaIndividualResponse> buscarCobrancas(
        CobrancaIndividualFiltroRequest filtro,
        Pageable pageable
    ) {
        return cobrancaRepository.findAll(
            CobrancaIndividualSpecifications.comFiltros(filtro),
            pageable
        ).map(CobrancaIndividualResponse::toDto);
    }

    @Transactional(readOnly = true)
    public Page<CobrancaLoteResponse> buscarLotes(UUID alunoId, Pageable pageable) {
        return cobrancaRepository.findLotesCobrancasPorAlunoId(
            alunoId,
            pageable
        ).map(CobrancaLoteResponse::toDto);
    }

    @Transactional(readOnly = true)
    public CobrancaLoteDetalheResponse buscarLotePorId(UUID loteId) {
        CobrancaLoteResponse lote = cobrancaRepository.findLoteCobrancaPorId(loteId)
            .map(CobrancaLoteResponse::toDto)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);

        List<CobrancaLoteDetalheResponse.CobrancaLoteItem> cobrancas = cobrancaRepository
            .findAllByLoteIdOrderByIdAsc(loteId)
            .stream()
            .map(CobrancaLoteDetalheResponse.CobrancaLoteItem::from)
            .toList();

        return CobrancaLoteDetalheResponse.from(lote, cobrancas);
    }

    @Transactional(readOnly = true)
    public CobrancaIndividualResponse buscarCobrancaPorId(Long cobrancaId) {
        return cobrancaRepository.findById(cobrancaId)
            .map(CobrancaIndividualResponse::toDto)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);
    }

    @Transactional
    public void registrarPagamento(RegistrarPagamentoIndividualRequest request) {
        List<Long> ids = request.cobrancaIds();
        if (ids.isEmpty()) {
            throw new CobrancaIndividualDadosInvalidosException("Informe ao menos uma cobrança");
        }
        if (ids.size() != ids.stream().distinct().count()) {
            throw new CobrancaIndividualDadosInvalidosException("Não informe a mesma cobrança mais de uma vez");
        }

        List<CobrancaIndividual> cobrancas = cobrancaRepository.findAllByIdInForUpdate(ids);
        if (cobrancas.size() != ids.size()) {
            throw new CobrancaIndividualNaoEncontradoException();
        }
        if (cobrancas.stream().map(CobrancaIndividual::getAlunoId).distinct().count() > 1) {
            throw new CobrancaIndividualDadosInvalidosException(
                "Todas as cobranças precisam pertencer ao mesmo aluno"
            );
        }

        UUID loteId = UUID.randomUUID();
        cobrancas.forEach(c -> c.registrarPagamento(loteId, request.formaPagamento(), request.comprovanteUrl()));
    }

    @Transactional
    public void cancelarPagamento(CancelarCobrancasIndividualRequest request) {
        List<Long> ids = request.cobrancaIds();
        if (ids.isEmpty()) {
            throw new CobrancaIndividualDadosInvalidosException("Informe ao menos uma cobrança");
        }
        if (ids.size() != ids.stream().distinct().count()) {
            throw new CobrancaIndividualDadosInvalidosException("Não informe a mesma cobrança mais de uma vez");
        }

        List<CobrancaIndividual> cobrancas = cobrancaRepository.findAllByIdInForUpdate(ids);
        if (cobrancas.size() != ids.size()) {
            throw new CobrancaIndividualNaoEncontradoException();
        }

        cobrancas.forEach(CobrancaIndividual::cancelarPagamento);
    }

    private static CobrancaResumo toResumo(CobrancaIndividual cobranca) {
        return new CobrancaResumo(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getValor(),
            cobranca.getStatus().name(),
            cobranca.getDataPagamento(),
            cobranca.getFormaPagamento() == null ? null : cobranca.getFormaPagamento().name(),
            cobranca.getComprovanteUrl(),
            cobranca.getLoteId()
        );
    }

    private static void validarDados(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        if (atendimentoId == null) {
            throw new CobrancaIndividualDadosInvalidosException("ID do atendimento é obrigatório");
        }
        if (alunoId == null) {
            throw new CobrancaIndividualDadosInvalidosException("ID do aluno é obrigatório");
        }
        if (valor == null) {
            throw new CobrancaIndividualDadosInvalidosException("Valor da cobrança é obrigatório");
        }
    }
}
