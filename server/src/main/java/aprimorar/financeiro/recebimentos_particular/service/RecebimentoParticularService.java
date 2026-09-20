package aprimorar.financeiro.recebimentos_particular.service;

import aprimorar.financeiro.cobrancas_particular.domain.CobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.domain.exception.CobrancaParticularNaoEncontradaException;
import aprimorar.financeiro.cobrancas_particular.repository.CobrancaParticularRepository;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_particular.domain.RecebimentoParticular;
import aprimorar.financeiro.recebimentos_particular.domain.exception.RecebimentoParticularDadosInvalidosException;
import aprimorar.financeiro.recebimentos_particular.domain.exception.RecebimentoParticularNaoEncontradoException;
import aprimorar.financeiro.recebimentos_particular.repository.RecebimentoParticularRepository;
import aprimorar.financeiro.recebimentos_particular.repository.RecebimentoParticularSpecifications;
import aprimorar.financeiro.recebimentos_particular.web.dto.RecebimentoParticularFiltroRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UUID registrarRecebimento(
        List<Long> cobrancaIds,
        LocalDate dataRecebimento,
        FormaPagamentoEnum formaPagamento,
        String comprovanteUrl
    ) {
        List<CobrancaParticular> cobrancas = cobrancaRepository
            .findAllByIdInForUpdate(cobrancaIds);

        if (cobrancas.size() != cobrancaIds.size()) {
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
                dataRecebimento,
                total,
                formaPagamento,
                comprovanteUrl
            )
        );

        cobrancas.forEach(cobranca -> cobranca.vincularRecebimento(recebimento));
        return recebimento.getId();
    }

    @Transactional(readOnly = true)
    public Page<RecebimentoParticular> buscarRecebimentos(
        RecebimentoParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        return recebimentoRepository.findAll(
            RecebimentoParticularSpecifications.comFiltros(filtro),
            pageable
        );
    }

    @Transactional(readOnly = true)
    public RecebimentoParticular buscarDetalhesPorId(UUID recebimentoId) {
        RecebimentoParticular recebimento = recebimentoRepository
            .findByIdWithCobrancas(recebimentoId)
            .orElseThrow(RecebimentoParticularNaoEncontradoException::new);

        if (recebimento.getCobrancas().isEmpty()) {
            throw new RecebimentoParticularDadosInvalidosException(
                "O recebimento não possui cobranças vinculadas"
            );
        }

        return recebimento;
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
