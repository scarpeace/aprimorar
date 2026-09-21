package aprimorar.financeiro.recebimentos_alunos.application;

import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaNaoEncontradaException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoNaoEncontradoException;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.CobrancaRepository;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.CobrancaSpecifications;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.RecebimentoRepository;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.RecebimentoSpecifications;
import aprimorar.financeiro.recebimentos_alunos.web.dto.CobrancaFiltroRequest;
import aprimorar.financeiro.recebimentos_alunos.web.dto.CobrancaResponse;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoDetalheResponse;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoFiltroRequest;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoResponse;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RegistrarRecebimentoRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecebimentoService {

    private final RecebimentoRepository recebimentoRepository;
    private final CobrancaRepository cobrancaRepository;

    public RecebimentoService(
        RecebimentoRepository recebimentoRepository,
        CobrancaRepository cobrancaRepository
    ) {
        this.recebimentoRepository = recebimentoRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional
    public UUID registrarRecebimento(RegistrarRecebimentoRequest dto) {
        List<Cobranca> cobrancas = cobrancaRepository
            .findAllByIdInForUpdate(dto.cobrancaIds());

        if (cobrancas.size() != dto.cobrancaIds().size()) {
            throw new CobrancaNaoEncontradaException();
        }

        if (cobrancas.stream().map(Cobranca::getAlunoId).distinct().count() > 1) {
            throw new RecebimentoDadosInvalidosException(
                "Todas as cobranças precisam pertencer ao mesmo aluno"
            );
        }

        BigDecimal total = cobrancas.stream()
            .map(Cobranca::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        CobrancaRecebimento recebimento = recebimentoRepository.save(
            new CobrancaRecebimento(
                dto.dataRecebimento(),
                total,
                dto.formaPagamento(),
                dto.comprovanteUrl()
            )
        );

        cobrancas.forEach(cobranca -> cobranca.vincularRecebimento(recebimento));
        return recebimento.getId();
    }

    @Transactional(readOnly = true)
    public Page<RecebimentoResponse> getRecebimentos(
        RecebimentoFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<CobrancaRecebimento> spec = RecebimentoSpecifications
            .comFiltros(filtro);

        return recebimentoRepository.findAll(spec, pageable)
            .map(RecebimentoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public RecebimentoDetalheResponse getRecebimentoPorId(
        UUID recebimentoId
    ) {
        CobrancaRecebimento recebimento = recebimentoRepository
            .findByIdWithCobrancas(recebimentoId)
            .orElseThrow(RecebimentoNaoEncontradoException::new);

        if (recebimento.getCobrancas().isEmpty()) {
            throw new RecebimentoDadosInvalidosException(
                "O recebimento não possui cobranças vinculadas"
            );
        }

        return RecebimentoDetalheResponse.toDto(recebimento);
    }

    @Transactional
    public void cancelarRecebimento(UUID recebimentoId) {
        if (recebimentoId == null) {
            throw new RecebimentoDadosInvalidosException(
                "ID do recebimento é obrigatório"
            );
        }

        CobrancaRecebimento recebimento = recebimentoRepository
            .findByIdForUpdate(recebimentoId)
            .orElseThrow(RecebimentoNaoEncontradoException::new);

        List<Cobranca> cobrancas = cobrancaRepository
            .findAllByRecebimentoIdForUpdate(recebimentoId);

        if (cobrancas.isEmpty()) {
            throw new RecebimentoDadosInvalidosException(
                "O recebimento não possui cobranças vinculadas"
            );
        }

        cobrancas.forEach(Cobranca::desvincularRecebimento);
        cobrancaRepository.flush();
        recebimentoRepository.delete(recebimento);
    }

    @Transactional(readOnly = true)
    public Page<CobrancaResponse> getCobrancas(
        CobrancaFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<Cobranca> spec = CobrancaSpecifications.comFiltros(filtro);
        return cobrancaRepository.findAll(spec, pageable).map(CobrancaResponse::toDto);
    }

    @Transactional(readOnly = true)
    public CobrancaResponse getCobrancaPorId(Long cobrancaId) {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
            .orElseThrow(CobrancaNaoEncontradaException::new);
        return CobrancaResponse.toDto(cobranca);
    }
}
