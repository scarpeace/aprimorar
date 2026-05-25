package aprimorar.atendimentos.api;

import aprimorar.atendimentos.api.dto.AlunoAtendimentosResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorAtendimentosResponseDTO;
import aprimorar.shared.PageDTO;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AtendimentosQueryApi {

    PageDTO<AtendimentoResponseDTO> getAtendimentos(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    );

    AtendimentoResponseDTO findAtendimentoById(UUID id);

    ColaboradorAtendimentosResponseDTO getAtendimentosByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    );

    AlunoAtendimentosResponseDTO getAtendimentosByStudentId(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    );

    AtendimentoFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate);

    boolean alunoHasPendingCharges(UUID alunoId);
    boolean colaboradorHasPendingPayment(UUID colaboradorId);
    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);
    long countAtendimentosInPeriod(Instant startDate, Instant endDate);
}
