package aprimorar.financeiro.pagamentos_particular.service;

import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularDadosInvalidosException;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularNaoEncontradoException;
import aprimorar.financeiro.pagamentos_particular.repository.PagamentoParticularRepository;
import aprimorar.financeiro.pagamentos_particular.repository.PagamentoParticularSpecifications;
import aprimorar.financeiro.pagamentos_particular.web.dto.PagamentoParticularFiltroRequest;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularNaoEncontradoException;
import aprimorar.financeiro.repasses_particular.repository.RepasseParticularRepository;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoParticularService {

    private final PagamentoParticularRepository pagamentoRepository;
    private final RepasseParticularRepository repasseRepository;

    public PagamentoParticularService(
        PagamentoParticularRepository pagamentoRepository,
        RepasseParticularRepository repasseRepository
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.repasseRepository = repasseRepository;
    }

    @Transactional(readOnly = true)
    public Page<PagamentoParticular> buscarPagamentos(
        PagamentoParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        return pagamentoRepository.findAll(
            PagamentoParticularSpecifications.comFiltros(filtro),
            pageable
        );
    }

    @Transactional(readOnly = true)
    public PagamentoParticular buscarDetalhesPorId(UUID pagamentoId) {
        PagamentoParticular pagamento = pagamentoRepository
            .findByIdWithRepasses(pagamentoId)
            .orElseThrow(PagamentoParticularNaoEncontradoException::new);

        if (pagamento.getRepasses().isEmpty()) {
            throw new PagamentoParticularDadosInvalidosException(
                "O pagamento não possui repasses vinculados"
            );
        }

        return pagamento;
    }

    @Transactional
    public UUID registrarPagamento(
        List<Long> repasseIds,
        LocalDate dataPagamento,
        FormaPagamentoEnum formaPagamento,
        String comprovanteUrl
    ) {

        List<RepasseParticular> repasses = repasseRepository.findAllByIdInForUpdate(repasseIds);
        if (repasses.size() != repasseIds.size()) {
            throw new RepasseParticularNaoEncontradoException();
        }

        if (repasses.stream().map(RepasseParticular::getColaboradorId).distinct().count() > 1) {
            throw new PagamentoParticularDadosInvalidosException("Todos os repasses precisam pertencer ao mesmo colaborador");
        }

        BigDecimal total = repasses.stream()
            .map(RepasseParticular::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagamentoParticular pagamento = pagamentoRepository.save(
            new PagamentoParticular(
                dataPagamento,
                total,
                formaPagamento,
                comprovanteUrl
            )
        );

        repasses.forEach(repasse -> repasse.vincularPagamento(pagamento));
        return pagamento.getId();
    }

    @Transactional
    public void cancelarPagamento(UUID pagamentoId) {

        PagamentoParticular pagamento = pagamentoRepository.findByIdForUpdate(pagamentoId)
            .orElseThrow(PagamentoParticularNaoEncontradoException::new);

        List<RepasseParticular> repasses = repasseRepository
            .findAllByPagamentoIdForUpdate(pagamentoId);

        if (repasses.isEmpty()) {
            throw new PagamentoParticularDadosInvalidosException(
                "O pagamento não possui repasses vinculados"
            );
        }

        repasses.forEach(RepasseParticular::desvincularPagamento);
        repasseRepository.flush();
        pagamentoRepository.delete(pagamento);
    }
}
