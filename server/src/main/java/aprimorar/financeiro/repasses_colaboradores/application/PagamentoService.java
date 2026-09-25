package aprimorar.financeiro.repasses_colaboradores.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoNaoEncontradoException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseNaoEncontradoException;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.PagamentoRepository;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.PagamentoSpecifications;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.RepasseRepository;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.RepasseSpecifications;
import aprimorar.financeiro.repasses_colaboradores.web.dto.PagamentoDetalheResponse;
import aprimorar.financeiro.repasses_colaboradores.web.dto.PagamentoFiltroRequest;
import aprimorar.financeiro.repasses_colaboradores.web.dto.PagamentoResponse;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RegistrarPagamentoRequest;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RepasseFiltroRequest;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RepasseResponse;

@Service
public class PagamentoService{

    private final PagamentoRepository pagamentoRepository;
    private final RepasseRepository repasseRepository;

    public PagamentoService(
        PagamentoRepository pagamentoRepository,
        RepasseRepository repasseRepository
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.repasseRepository = repasseRepository;
    }

    @Transactional(readOnly = true)
    public Page<PagamentoResponse> getPagamentos(
        PagamentoFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<RepassePagamento> spec = PagamentoSpecifications.comFiltros(filtro);
        return pagamentoRepository.findAll(spec, pageable).map(PagamentoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public PagamentoDetalheResponse getPagamentoPorId(UUID pagamentoId) {
        RepassePagamento pagamento = pagamentoRepository.findByIdWithRepasses(pagamentoId)
            .orElseThrow(PagamentoNaoEncontradoException::new);

        return PagamentoDetalheResponse.toDto(pagamento);
    }

    @Transactional
    public UUID registrarPagamento(RegistrarPagamentoRequest dto) {
        List<Repasse> repasses = repasseRepository.findAllByIdInForUpdate(dto.repasseIds());

        if (repasses.size() != dto.repasseIds().size()) {
            throw new RepasseNaoEncontradoException();
        }

        if (repasses.stream().map(Repasse::getColaboradorId).distinct().count() > 1) {
            throw new PagamentoDadosInvalidosException("Todos os repasses precisam pertencer ao mesmo colaborador");
        }

        BigDecimal total = repasses.stream()
            .map(Repasse::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        RepassePagamento pagamento = pagamentoRepository.save(
            new RepassePagamento(
                dto.dataPagamento(),
                total,
                dto.formaPagamento(),
                dto.comprovanteUrl()
            )
        );

        repasses.forEach(repasse -> repasse.vincularPagamento(pagamento));
        return pagamento.getId();
    }

    @Transactional
    public void cancelarPagamento(UUID pagamentoId) {

        RepassePagamento pagamento = pagamentoRepository.findByIdForUpdate(pagamentoId)
            .orElseThrow(PagamentoNaoEncontradoException::new);

        List<Repasse> repasses = repasseRepository
            .findAllByPagamentoIdForUpdate(pagamentoId);

        if (repasses.isEmpty()) {
            throw new PagamentoDadosInvalidosException(
                "O pagamento não possui repasses vinculados"
            );
        }

        repasses.forEach(Repasse::desvincularPagamento);
        repasseRepository.flush();
        pagamentoRepository.delete(pagamento);
    }

    @Transactional(readOnly = true)
    public Page<RepasseResponse> getRepasses(RepasseFiltroRequest filtro, Pageable pageable) {
        Specification<Repasse> spec = RepasseSpecifications.comFiltros(filtro);
        return repasseRepository.findAll(spec, pageable).map(RepasseResponse::toDto);
    }

    @Transactional(readOnly = true)
    public RepasseResponse findRepasseById(Long id) {
        Repasse repasse = repasseRepository.findById(id)
            .orElseThrow(RepasseNaoEncontradoException::new);
        return RepasseResponse.toDto(repasse);
    }

    @Transactional
    public int marcarRepassesAtrasados() {
        return repasseRepository.marcarAtrasados(
            StatusRepasse.PENDENTE,
            StatusRepasse.ATRASADO,
            LocalDateTime.now().minusDays(30)
        );
    }
}
