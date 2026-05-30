package aprimorar.atendimentos.internal.application;

import aprimorar.atendimentos.internal.dto.AtendimentosAlunosKpisDTO;
import aprimorar.atendimentos.internal.dto.AtendimentosColaboradorKpisDTO;
import aprimorar.atendimentos.internal.dto.AtendimentosContentReportDTO;
import aprimorar.atendimentos.internal.dto.AtendimentosKpisDTO;
import aprimorar.atendimentos.internal.infrastructure.persistence.AtendimentoRepository;
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
        BigDecimal totalStudentCharged = atendimentoRepo.sumTotalChargedInPeriod(startDate, endDate);
        BigDecimal totalStudentPending = atendimentoRepo.sumTotalUnchargedInPeriod(startDate, endDate);
        BigDecimal totalEmployeePaid = atendimentoRepo.sumTotalPaidInPeriod(startDate, endDate);
        BigDecimal totalEmployeePending = atendimentoRepo.sumTotalUnpaidInPeriod(startDate, endDate);

        return new AtendimentosKpisDTO(
            totalStudentCharged,
            totalStudentPending,
            totalEmployeePaid,
            totalEmployeePending
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
                item.getEmployeeId(),
                item.getTotalAtendimentos(),
                item.getTotalPaid(),
                item.getTotalPending()
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
                item.getStudentId(),
                item.getTotalAtendimentos(),
                item.getTotalCharged(),
                item.getTotalPending()
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
