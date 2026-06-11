package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AtendimentosAlunosKpisDTO;
import aprimorar.atendimentos.dto.AtendimentosColaboradorKpisDTO;
import aprimorar.atendimentos.dto.AtendimentosContentReportDTO;
import aprimorar.atendimentos.dto.AtendimentosKpisDTO;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoReportService {

    private final AtendimentoRepository atendimentoRepo;

    public AtendimentoReportService(AtendimentoRepository atendimentoRepo) {
        this.atendimentoRepo = atendimentoRepo;
    }

    @Transactional(readOnly = true)
    public AtendimentosKpisDTO getKpisAtendimentos(Instant startDate, Instant endDate) {
        BigDecimal totalCobradoAlunos = atendimentoRepo.sumTotalCobradoInPeriod(startDate, endDate);
        BigDecimal totalPendenteAlunos = atendimentoRepo.sumTotalPendenteAlunoInPeriod(startDate, endDate);
        BigDecimal totalPagoColaboradores = atendimentoRepo.sumTotalPagoInPeriod(startDate, endDate);
        BigDecimal totalPendenteColaboradores = atendimentoRepo.sumTotalPendenteColaboradorInPeriod(startDate, endDate);

        return new AtendimentosKpisDTO(
            totalCobradoAlunos,
            totalPendenteAlunos,
            totalPagoColaboradores,
            totalPendenteColaboradores
        );
    }

    @Transactional(readOnly = true)
    public PageDTO<AtendimentosColaboradorKpisDTO> getKpisAtendimentosColaboradores(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    ) {
        Page<AtendimentosColaboradorKpisDTO> resultado = atendimentoRepo
            .getOverviewFinanceiroColaboradores(pageable, startDate, endDate)
            .map(item -> new AtendimentosColaboradorKpisDTO(
                item.getColaboradorId(),
                item.getTotalAtendimentos(),
                item.getTotalPago(),
                item.getTotalPendente()
            ));

        return new PageDTO<>(resultado);
    }

    @Transactional(readOnly = true)
    public PageDTO<AtendimentosAlunosKpisDTO> getKpisAtendimentosAlunos(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    ) {
        Page<AtendimentosAlunosKpisDTO> resultado = atendimentoRepo
            .getOverviewFinanceiroAlunos(pageable, startDate, endDate)
            .map(item -> new AtendimentosAlunosKpisDTO(
                item.getAlunoId(),
                item.getTotalAtendimentos(),
                item.getTotalCobrado(),
                item.getTotalPendente()
            ));

        return new PageDTO<>(resultado);
    }

    @Transactional(readOnly = true)
    public AtendimentosContentReportDTO getAtendimentosContentReport(
        Instant startDate,
        Instant endDate
    ) {
        var report = atendimentoRepo.getAtendimentosContentReport(startDate, endDate);

        return new AtendimentosContentReportDTO(
            report.getTotalAulas(),
            report.getTotalMentoria(),
            report.getTotalTerapia(),
            report.getTotalOV(),
            report.getTotalENEM(),
            report.getTotalPAS(),
            report.getTotalOutros()
        );
    }
}
