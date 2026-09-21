package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.service;

import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.CobrancaParticular;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularNaoEncontradaException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.repository.CobrancaParticularRepository;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.RecebimentoParticular;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.exception.RecebimentoParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.exception.RecebimentoParticularNaoEncontradoException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.repository.RecebimentoParticularRepository;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.repository.RecebimentoParticularSpecifications;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web.dto.RecebimentoParticularDetalheResponse;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web.dto.RecebimentoParticularFiltroRequest;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web.dto.RecebimentoParticularResponse;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web.dto.RegistrarRecebimentoParticularRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecebimentoParticularService {

    private final RecebimentoParticularRepository recebimentoRepository;
    private final CobrancaParticularRepository cobrancaRepository;

    public RecebimentoParticularService(
        RecebimentoParticularRepository recebimentoRepository,
        CobrancaParticularRepository cobrancaRepository
    ) {
        this.recebimentoRepository = recebimentoRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional
    public UUID registrarRecebimento(RegistrarRecebimentoParticularRequest dto) {
        List<CobrancaParticular> cobrancas = cobrancaRepository
            .findAllByIdInForUpdate(dto.cobrancaIds());

        if (cobrancas.size() != dto.cobrancaIds().size()) {
            throw new CobrancaParticularNaoEncontradaException();
        }

        if (cobrancas.stream().map(CobrancaParticular::getAlunoId).distinct().count() > 1) {
            throw new RecebimentoParticularDadosInvalidosException(
                "Todas as cobranças precisam pertencer ao mesmo aluno"
            );
        }

        BigDecimal total = cobrancas.stream()
            .map(CobrancaParticular::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        RecebimentoParticular recebimento = recebimentoRepository.save(
            new RecebimentoParticular(
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
    public Page<RecebimentoParticularResponse> getRecebimentos(
        RecebimentoParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<RecebimentoParticular> spec = RecebimentoParticularSpecifications
            .comFiltros(filtro);

        return recebimentoRepository.findAll(spec, pageable)
            .map(RecebimentoParticularResponse::toDto);
    }

    @Transactional(readOnly = true)
    public RecebimentoParticularDetalheResponse getRecebimentoPorId(
        UUID recebimentoId
    ) {
        RecebimentoParticular recebimento = recebimentoRepository
            .findByIdWithCobrancas(recebimentoId)
            .orElseThrow(RecebimentoParticularNaoEncontradoException::new);

        if (recebimento.getCobrancas().isEmpty()) {
            throw new RecebimentoParticularDadosInvalidosException(
                "O recebimento não possui cobranças vinculadas"
            );
        }

        return RecebimentoParticularDetalheResponse.toDto(recebimento);
    }

    @Transactional
    public void cancelarRecebimento(UUID recebimentoId) {
        if (recebimentoId == null) {
            throw new RecebimentoParticularDadosInvalidosException(
                "ID do recebimento é obrigatório"
            );
        }

        RecebimentoParticular recebimento = recebimentoRepository
            .findByIdForUpdate(recebimentoId)
            .orElseThrow(RecebimentoParticularNaoEncontradoException::new);

        List<CobrancaParticular> cobrancas = cobrancaRepository
            .findAllByRecebimentoIdForUpdate(recebimentoId);

        if (cobrancas.isEmpty()) {
            throw new RecebimentoParticularDadosInvalidosException(
                "O recebimento não possui cobranças vinculadas"
            );
        }

        cobrancas.forEach(CobrancaParticular::desvincularRecebimento);
        cobrancaRepository.flush();
        recebimentoRepository.delete(recebimento);
    }
}
