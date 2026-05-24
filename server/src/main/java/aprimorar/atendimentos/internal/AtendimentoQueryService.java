package aprimorar.atendimentos.internal;

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

import aprimorar.atendimentos.api.AtendimentosQueryApi;
import aprimorar.atendimentos.api.dto.AlunoAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.AlunoSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorSummaryDTO;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.atendimentos.internal.repository.AtendimentoSpecifications;
import aprimorar.financeiro.api.FinanceiroService;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

@Service
class AtendimentoQueryService implements AtendimentosQueryApi {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoQueryService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AtendimentoMapper appointmentMapper;
    private final FinanceiroService expenseService;
    private final AlunoService studentService;
    private final ColaboradorQueryApi colaboradorService;

    AtendimentoQueryService(
        AtendimentoRepository atendimentoRepo,
        AtendimentoMapper appointmentMapper,
        FinanceiroService expenseService,
        AlunoService studentService,
        ColaboradorQueryApi colaboradorService
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.appointmentMapper = appointmentMapper;
        this.expenseService = expenseService;
        this.studentService = studentService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional(readOnly = true)
    public PageDTO<AtendimentoResponseDTO> getAtendimentos(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    ) {
        Boolean chargedFilter = Boolean.TRUE.equals(hideCharged) ? Boolean.FALSE : null;
        Boolean paidFilter = Boolean.TRUE.equals(hidePaid) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.searchContains(search)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withStudentCharged(chargedFilter))
            .and(AtendimentoSpecifications.withEmployeePaid(paidFilter));

        Page<Atendimento> appointmentPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        log.info("Consulta de appointments finalizada, {} registros encontrados.", appointmentPage.getTotalElements());
        return new PageDTO<>(dtoPage);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponseDTO findAtendimentoById(UUID id) {
        Atendimento appointment = atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
        log.info("Atendimento {} consultado com sucesso.", appointment.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional(readOnly = true)
    public ColaboradorAppointmentsResponseDTO getAppointmentsByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    ) {
        colaboradorService.findColaboradorById(employeeId);
        Boolean paidFilter = Boolean.TRUE.equals(hidePaid) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.withEmployeeId(employeeId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withEmployeePaid(paidFilter));

        Page<Atendimento> appointmentPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        long totalEvents = atendimentoRepo.countFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalPaid = atendimentoRepo.sumPaidFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalUnpaid = atendimentoRepo.sumUnpaidFilteredByEmployeeId(employeeId, startDate, endDate);

        log.info("Consulta de appointments do colaborador finalizada, {} registros encontrados.", appointmentPage.getTotalElements());

        return new ColaboradorAppointmentsResponseDTO(
            new PageDTO<>(dtoPage),
            new ColaboradorSummaryDTO(totalEvents, totalPaid, totalUnpaid)
        );
    }

    @Transactional(readOnly = true)
    public AlunoAppointmentsResponseDTO getAppointmentsByStudentId(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    ) {
        studentService.findByAlunoId(studentId);

        Specification<Atendimento> spec = AtendimentoSpecifications.withStudentId(studentId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withStudentCharged(charged));

        Page<Atendimento> appointmentPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        log.info("Consulta de appointments do aluno finalizada, {} registros encontrados.", appointmentPage.getTotalElements());

        long totalEvents = atendimentoRepo.countFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalCharged = atendimentoRepo.sumChargedFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalPending = atendimentoRepo.sumPendingFilteredByStudentId(studentId, startDate, endDate);

        return new AlunoAppointmentsResponseDTO(new PageDTO<>(dtoPage), new AlunoSummaryDTO(totalEvents, totalCharged, totalPending));
    }

    @Transactional(readOnly = true)
    public AtendimentoFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate) {
        BigDecimal totalStudentCharged = atendimentoRepo.sumChargedFiltered(startDate, endDate);
        BigDecimal totalStudentPending = atendimentoRepo.sumPendingFiltered(startDate, endDate);
        BigDecimal totalEmployeePaid = atendimentoRepo.sumPaidFiltered(startDate, endDate);
        BigDecimal totalEmployeePending = atendimentoRepo.sumUnpaidFiltered(startDate, endDate);
        BigDecimal totalGeneralExpenses = expenseService.sumExpenses(startDate, endDate);
        BigDecimal balance = totalStudentCharged.subtract(totalEmployeePaid).subtract(totalGeneralExpenses);

        return new AtendimentoFinanceSummaryDTO(
            totalStudentCharged,
            totalStudentPending,
            totalEmployeePaid,
            totalEmployeePending,
            totalGeneralExpenses,
            balance
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId) {
        return atendimentoRepo.countDistinctStudentsInPeriodExcludingAluno(startDate, endDate, excludedStudentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAppointmentsInPeriod(Instant startDate, Instant endDate) {
        return atendimentoRepo.countByStartDateGreaterThanEqualAndStartDateLessThan(startDate, endDate);
    }

    @Override
    public boolean colaboradorHasPendingPayment(UUID colaboradorId) {
        return atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(colaboradorId);
    }

    @Override
    public boolean alunoHasPendingCharges(UUID alunoId) {
        return atendimentoRepo.existsByStudentIdAndStudentPaymentDateIsNull(alunoId);
    }
}
