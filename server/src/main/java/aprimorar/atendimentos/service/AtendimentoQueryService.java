package aprimorar.atendimentos.service;

import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

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
import aprimorar.atendimentos.dto.AlunoResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.CalendarioAtendimentosResponse;
import aprimorar.atendimentos.dto.CalendarioMensalAtendimentosResponse;
import aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.RelatorioAtendimentosResponse;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.atendimentos.repository.specifications.AtendimentoSpecifications;
import aprimorar.exception.BusinessException;
import java.util.UUID;

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

        log.info("Atendimento {} consultado com sucesso.", atendimento.getId());
        return AtendimentoResponse.toDto(atendimento);
    }

    @Transactional(readOnly = true)
    public CalendarioMensalAtendimentosResponse getCalendarioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);
        var relatorio = getRelatorioAtendimentos(mes);
        List<CalendarioAtendimentosResponse> eventos = atendimentoRepo.getCalendarioMensal(dataInicio, dataFim);

        return new CalendarioMensalAtendimentosResponse(
            mes,
            relatorio.totalAtendimentos(),
            relatorio.totalAulas(),
            relatorio.totalMentoria(),
            relatorio.totalTerapia(),
            relatorio.totalOV(),
            relatorio.totalENEM(),
            relatorio.totalPAS(),
            relatorio.totalOutros(),
            eventos
        );
    }

    @Transactional(readOnly = true)
    public RelatorioAtendimentosResponse getRelatorioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        AtendimentosReportProjection resumo = atendimentoRepo.getRelatorioMensal(dataInicio, dataFim);

        var totalAtendimentos = resumo.getTotalAulas() + resumo.getTotalMentoria() + resumo.getTotalTerapia() + resumo.getTotalOV() + resumo.getTotalENEM() + resumo.getTotalPAS() + resumo.getTotalOutros();

        return new RelatorioAtendimentosResponse(
            totalAtendimentos,
            resumo.getTotalAulas(),
            resumo.getTotalMentoria(),
            resumo.getTotalTerapia(),
            resumo.getTotalOV(),
            resumo.getTotalENEM(),
            resumo.getTotalPAS(),
            resumo.getTotalOutros()
        );
    }

    @Transactional(readOnly = true)
    public ColaboradorResumoFinanceiroResponse getResumoFinanceiroColaborador(UUID colaboradorId, YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        return new ColaboradorResumoFinanceiroResponse(
            atendimentoRepo.getTotalRepassePagoColaborador(colaboradorId, dataInicio, dataFim),
            atendimentoRepo.getTotalRepassePendenteColaborador(colaboradorId, dataInicio, dataFim)
        );
    }

    @Transactional(readOnly = true)
    public AlunoResumoFinanceiroResponse getResumoFinanceiroAluno(UUID alunoId, YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        return new AlunoResumoFinanceiroResponse(
            atendimentoRepo.getTotalPagamentoPagoAluno(alunoId, dataInicio, dataFim),
            atendimentoRepo.getTotalPagamentoPendenteAluno(alunoId, dataInicio, dataFim)
        );
    }
}
