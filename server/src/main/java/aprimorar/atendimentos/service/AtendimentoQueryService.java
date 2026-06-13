package aprimorar.atendimentos.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.dto.AlunoAtendimentosKpis;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.dto.ColaboradorAtendimentosKpis;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.repository.specifications.AtendimentoSpecifications;
import aprimorar.pessoas.api.AlunoQueryApi;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

@Service
public class AtendimentoQueryService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoQueryService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AlunoQueryApi alunoQueryApi;

    public AtendimentoQueryService(
        AtendimentoRepository atendimentoRepo,
        AlunoQueryApi alunoQueryApi
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoQueryApi = alunoQueryApi;
    }

    @Transactional(readOnly = true)
    public PageDTO<AtendimentoResponseDTO> getAtendimentos(Pageable pageable,AtendimentoFiltroRequest filtro) {
        Specification<Atendimento> spec = AtendimentoSpecifications.comFiltros(filtro);
        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);

        Page<AtendimentoResponseDTO> dtoPage = atendimentoPage.map(AtendimentoResponseDTO::toDto);

        log.info("Consulta de atendimentos finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());
        return new PageDTO<>(dtoPage);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponseDTO findAtendimentoById(UUID id) {
        Atendimento atendimento = atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
        log.info("Atendimento {} consultado com sucesso.", atendimento.getTitulo().toUpperCase());
        return AtendimentoResponseDTO.toDto(atendimento);
    }

    @Transactional(readOnly = true)
    public AtendimentosColaboradorResponseDTO getAtendimentosByColaborador(
        Pageable pageable,
        UUID colaboradorId,
        Boolean ocultarPagos,
        Instant inicio,
        Instant fim
    ) {
        Boolean pago = Boolean.TRUE.equals(ocultarPagos) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.colaboradorIdIgual(colaboradorId)
            .and(AtendimentoSpecifications.inicioMaiorOuIgual(inicio))
            .and(AtendimentoSpecifications.fimMenorOuIgual(fim))
            .and(AtendimentoSpecifications.colaboradorPagoContem(pago));

        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentoPage.map(AtendimentoResponseDTO::toDto);

        long totalAtendimentos = atendimentoRepo.countByColaboradorIdInPeriod(colaboradorId, inicio, fim);
        BigDecimal totalPago = atendimentoRepo.sumPagoByColaboradorIdInPeriod(colaboradorId, inicio, fim);
        BigDecimal totalPendente = atendimentoRepo.sumPendenteByColaboradorIdInPeriod(colaboradorId, inicio, fim);

        log.info("Consulta de atendimentos do colaborador finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());

        return new AtendimentosColaboradorResponseDTO(
            new PageDTO<>(dtoPage),
            new ColaboradorAtendimentosKpis(totalAtendimentos, totalPago, totalPendente)
        );
    }

    @Transactional(readOnly = true)
    public AtendimentosAlunoResponseDTO getAtendimentosByAluno(
        Pageable pageable,
        UUID alunoId,
        Instant inicio,
        Instant fim,
        Boolean cobrado
    ) {
        alunoQueryApi.findAlunoById(alunoId);

        Specification<Atendimento> spec = AtendimentoSpecifications.alunoIdIgual(alunoId)
            .and(AtendimentoSpecifications.inicioMaiorOuIgual(inicio))
            .and(AtendimentoSpecifications.fimMenorOuIgual(fim))
            .and(AtendimentoSpecifications.alunoCobradoContem(cobrado));

        Page<Atendimento> atendimentos = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentos.map(AtendimentoResponseDTO::toDto);

        long totalAtendimentos = atendimentoRepo.countByAlunoIdInPeriod(alunoId, inicio, fim);
        BigDecimal totalCobrado = atendimentoRepo.sumCobradoByAlunoInPeriod(alunoId, inicio, fim);
        BigDecimal totalPendente = atendimentoRepo.sumPendenteByAlunoInPeriod(alunoId, inicio, fim);

        return new AtendimentosAlunoResponseDTO(new PageDTO<>(dtoPage), new AlunoAtendimentosKpis(totalAtendimentos, totalCobrado, totalPendente));
    }

}
