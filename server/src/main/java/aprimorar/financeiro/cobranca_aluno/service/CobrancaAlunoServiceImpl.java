package aprimorar.financeiro.cobranca_aluno.service;

import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import aprimorar.financeiro.cobranca_aluno.domain.StatusCobrancaAluno;
import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoNaoEncontradoException;
import aprimorar.financeiro.cobranca_aluno.repository.CobrancaAlunoRepository;
import aprimorar.financeiro.cobranca_aluno.web.dto.CancelarCobrancasAlunoRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoItemRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoResponse;
import aprimorar.financeiro.cobranca_aluno.web.dto.PagarCobrancasAlunoRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CobrancaAlunoServiceImpl {

    private final CobrancaAlunoRepository cobrancaRepository;

    public CobrancaAlunoServiceImpl(CobrancaAlunoRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional
    public List<CobrancaAlunoResponse> registrarPagamento(PagarCobrancasAlunoRequest request) {
        List<CobrancaAlunoItemRequest> itens = request.cobrancas();
        List<Long> ids = itens.stream().map(CobrancaAlunoItemRequest::cobrancaId).toList();

        if (ids.size() != ids.stream().distinct().count()) {
            throw new CobrancaAlunoDadosInvalidosException("Não informe a mesma cobrança mais de uma vez");
        }

        List<CobrancaAlunoEntity> cobrancas = cobrancaRepository.findAllByIdInForUpdate(ids);
        if (cobrancas.size() != ids.size()) {
            throw new CobrancaAlunoNaoEncontradoException();
        }
        if (cobrancas.stream().anyMatch(cobranca -> cobranca.getStatus() != StatusCobrancaAluno.PENDENTE)) {
            throw new CobrancaAlunoDadosInvalidosException("Todas as cobranças precisam estar pendentes");
        }
        if (cobrancas.stream().map(CobrancaAlunoEntity::getAlunoId).distinct().count() > 1) {
            throw new CobrancaAlunoDadosInvalidosException("Todas as cobranças precisam pertencer ao mesmo aluno");
        }

        Map<Long, CobrancaAlunoItemRequest> itensPorId = itens.stream()
            .collect(Collectors.toMap(CobrancaAlunoItemRequest::cobrancaId, Function.identity()));

        cobrancas.forEach(cobranca -> {
            CobrancaAlunoItemRequest item = itensPorId.get(cobranca.getId());
            cobranca.registrarPagamento(item.desconto(), request.formaPagamento(), request.comprovanteUrl());
        });

        return cobrancaRepository.saveAll(cobrancas).stream()
            .map(CobrancaAlunoResponse::from)
            .toList();
    }

    @Transactional
    public List<CobrancaAlunoResponse> cancelarPagamento(CancelarCobrancasAlunoRequest request) {
        List<Long> ids = request.cobrancaIds();

        if (ids.size() != ids.stream().distinct().count()) {
            throw new CobrancaAlunoDadosInvalidosException("Não informe a mesma cobrança mais de uma vez");
        }

        List<CobrancaAlunoEntity> cobrancas = cobrancaRepository.findAllByIdInForUpdate(ids);
        if (cobrancas.size() != ids.size()) {
            throw new CobrancaAlunoNaoEncontradoException();
        }
        if (cobrancas.stream().anyMatch(cobranca -> cobranca.getStatus() != StatusCobrancaAluno.PAGO)) {
            throw new CobrancaAlunoDadosInvalidosException("Todas as cobranças precisam estar pagas");
        }

        cobrancas.forEach(CobrancaAlunoEntity::cancelarPagamento);
        return cobrancaRepository.saveAll(cobrancas).stream()
            .map(CobrancaAlunoResponse::from)
            .toList();
    }
}
