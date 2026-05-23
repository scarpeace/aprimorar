package aprimorar.atendimentos.internal;

import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradoresFinanceSummaryResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradoresWithFinanceResponseDTO;
import aprimorar.atendimentos.api.dto.AlunoAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.AlunosFinanceSummaryResponseDTO;
import aprimorar.atendimentos.api.dto.AlunosWithFinanceResponseDTO;
import aprimorar.shared.PageDTO;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface AtendimentoManagementService {

    AtendimentoResponseDTO createAppointment(AtendimentoRequestDTO dto);

    PageDTO<AtendimentoResponseDTO> getAppointments(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    );

    AtendimentoResponseDTO findById(UUID id);

    ColaboradorAppointmentsResponseDTO getAppointmentsByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    );

    AlunoAppointmentsResponseDTO getAppointmentsByStudentId(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    );

    AtendimentoFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate);

    AlunosFinanceSummaryResponseDTO getStudentsFinanceReport(Instant startDate, Instant endDate);

    ColaboradoresFinanceSummaryResponseDTO getEmployeesFinanceReport(Instant startDate, Instant endDate);

    ColaboradoresWithFinanceResponseDTO getEmployeesWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    );

    AlunosWithFinanceResponseDTO getStudentsWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    );

    AtendimentoResponseDTO updateAppointment(UUID id, AtendimentoRequestDTO dto);

    void deleteAppointment(UUID id);

    AtendimentoResponseDTO toggleStudentCharge(UUID id);

    AtendimentoResponseDTO toggleEmployeePayment(UUID id);
}
