package aprimorar.financeiro.pagamentos_particular.service;

import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularDadosInvalidosException;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularNaoEncontradoException;
import aprimorar.financeiro.pagamentos_particular.repository.PagamentoParticularRepository;
import aprimorar.financeiro.pagamentos_particular.repository.PagamentoParticularSpecifications;
import aprimorar.financeiro.pagamentos_particular.web.dto.PagamentoParticularDetalheResponse;
import aprimorar.financeiro.pagamentos_particular.web.dto.PagamentoParticularFiltroRequest;
import aprimorar.financeiro.pagamentos_particular.web.dto.PagamentoParticularResponse;
import aprimorar.financeiro.pagamentos_particular.web.dto.RegistrarPagamentoParticularRequest;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularNaoEncontradoException;
import aprimorar.financeiro.repasses_particular.repository.RepasseParticularRepository;
import java.math.BigDecimal;
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
    public Page<PagamentoParticularResponse> buscarPagamentos(
        PagamentoParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        return pagamentoRepository.findAll(PagamentoParticularSpecifications.comFiltros(filtro),pageable)
        .map(PagamentoParticularResponse::from);
    }

    @Transactional(readOnly = true)
    public PagamentoParticularDetalheResponse buscarPorId(UUID pagamentoId) {

        PagamentoParticular pagamento = pagamentoRepository.findById(pagamentoId)
            .orElseThrow(PagamentoParticularNaoEncontradoException::new);
        List<RepasseParticular> repasses = repasseRepository
            .findAllByPagamentoIdOrderByIdAsc(pagamentoId);

        return PagamentoParticularDetalheResponse.from(
            pagamento,
            repasses.getFirst().getColaboradorId(),
            repasses
        );
    }

    @Transactional
    public UUID registrarPagamento(RegistrarPagamentoParticularRequest request) {

        List<RepasseParticular> repasses = repasseRepository.findAllByIdInForUpdate(request.repasseIds());
        if (repasses.size() != request.repasseIds().size()) {
            throw new RepasseParticularNaoEncontradoException();
        }

        if (repasses.stream().map(RepasseParticular::getColaboradorId).distinct().count() > 1) {
            throw new PagamentoParticularDadosInvalidosException("Todos os repasses precisam pertencer ao mesmo colaborador");
        }

        repasses.forEach(RepasseParticular::validarDisponivelParaPagamento);

        BigDecimal total = repasses.stream()
            .map(RepasseParticular::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagamentoParticular pagamento = pagamentoRepository.save(
            new PagamentoParticular(
                request.dataPagamento(),
                total,
                request.formaPagamento(),
                request.comprovanteUrl()
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
