package aprimorar.financeiro.cobranca_aluno.service;

import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import aprimorar.financeiro.cobranca_aluno.domain.StatusCobrancaAluno;
import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoNaoEncontradoException;
import aprimorar.financeiro.cobranca_aluno.repository.CobrancaAlunoRepository;
import aprimorar.financeiro.cobranca_aluno.repository.specifications.CobrancaAlunoSpecifications;
import aprimorar.financeiro.cobranca_aluno.web.dto.CancelarCobrancasAlunoRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoItemRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoFiltroRequest;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoResponse;
import aprimorar.financeiro.cobranca_aluno.web.dto.PagarCobrancasAlunoRequest;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CobrancaAlunoServiceImpl {

    private final CobrancaAlunoRepository cobrancaRepository;

    public CobrancaAlunoServiceImpl(CobrancaAlunoRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional(readOnly = true)
    public Page<CobrancaAlunoResponse> buscarCobrancas(
        CobrancaAlunoFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<CobrancaAlunoEntity> specification =
            CobrancaAlunoSpecifications.comFiltros(filtro);

        return cobrancaRepository.findAll(specification, pageable)
            .map(CobrancaAlunoResponse::from);
    }

    @Transactional(readOnly = true)
    public CobrancaAlunoResponse buscarCobrancaPorId(Long cobrancaId) {
        CobrancaAlunoEntity cobranca = cobrancaRepository.findById(cobrancaId)
            .orElseThrow(CobrancaAlunoNaoEncontradoException::new);

        return CobrancaAlunoResponse.from(cobranca);
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

        cobrancas.forEach(cobranca ->
            cobranca.registrarPagamento(request.formaPagamento(), request.comprovanteUrl())
        );

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
