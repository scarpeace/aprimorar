package aprimorar.atendimentos.service;

import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import aprimorar.atendimentos.dto.CalendarioAtendimentosRespose;
import aprimorar.atendimentos.dto.RelatorioAtendimentosResponse;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.atendimentos.repository.projections.AtendimentoCalendarioProjection;
import aprimorar.atendimentos.repository.specifications.AtendimentoSpecifications;
import aprimorar.shared.exception.BusinessException;

@Service
public class AtendimentoQueryService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoQueryService.class);

    private final AtendimentoRepository atendimentoRepo;

    public AtendimentoQueryService(AtendimentoRepository atendimentoRepo) {
        this.atendimentoRepo = atendimentoRepo;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoResponse> getAtendimentos(Pageable pageable,AtendimentoFiltroRequest filtro) {
        Specification<Atendimento> spec = AtendimentoSpecifications.comFiltros(filtro);
        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);

        log.info("Consulta de atendimentos finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());
        return atendimentoPage.map(AtendimentoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponse findAtendimentoById(Long id) {
        Atendimento atendimento = atendimentoRepo.findById(id).orElseThrow(
            () -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento não encontrado"));

        log.info("Atendimento {} consultado com sucesso.", atendimento.getTitulo().toUpperCase());
        return AtendimentoResponse.toDto(atendimento);
    }

    @Transactional(readOnly = true)
    public List<CalendarioAtendimentosRespose> getCalendarioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        List<AtendimentoCalendarioProjection> atendimentos = atendimentoRepo.getCalendarioMensal(dataInicio, dataFim);

        return atendimentos.stream().map(CalendarioAtendimentosRespose::toDto).toList();
    }

    @Transactional(readOnly = true)
    public RelatorioAtendimentosResponse getRelatorioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        AtendimentosReportProjection resumo = atendimentoRepo.getRelatorioMensal(dataInicio, dataFim);

        var totalAtendimentos = resumo.getTotalAulas() + resumo.getTotalMentoria() + resumo.getTotalTerapia() + resumo.getTotalOV() + resumo.getTotalENEM() + resumo.getTotalPAS() + resumo.getTotalOutros();
        var porcentagemAulas = (double) resumo.getTotalAulas() / totalAtendimentos * 100;
        var porcentagemMentoria = (double) resumo.getTotalMentoria() / totalAtendimentos * 100;
        var porcentagemTerapia = (double) resumo.getTotalTerapia() / totalAtendimentos * 100;
        var porcentagemOV = (double) resumo.getTotalOV() / totalAtendimentos * 100;
        var porcentagemENEM = (double) resumo.getTotalENEM() / totalAtendimentos * 100;
        var porcentagemPAS = (double) resumo.getTotalPAS() / totalAtendimentos * 100;
        var porcentagemOutros = (double) resumo.getTotalOutros() / totalAtendimentos * 100;

        return new RelatorioAtendimentosResponse(
            totalAtendimentos,
            resumo.getTotalAulas(),
            porcentagemAulas,
            resumo.getTotalMentoria(),
            porcentagemMentoria,
            resumo.getTotalTerapia(),
            porcentagemTerapia,
            resumo.getTotalOV(),
            porcentagemOV,
            resumo.getTotalENEM(),
            porcentagemENEM,
            resumo.getTotalPAS(),
            porcentagemPAS,
            resumo.getTotalOutros(),
            porcentagemOutros
        );
    }
}
