package aprimorar.atendimentos.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.pessoas.colaborador.api.contract.ColaboradorReadApi;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorResponseDTO;
import aprimorar.atendimentos.api.AtendimentoService;
import aprimorar.atendimentos.api.dto.ContentDistributionDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.ColaboradoresFinanceSummaryResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradoresWithFinanceResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorWithFinanceDTO;
import aprimorar.atendimentos.api.dto.ColaboradorSummaryDTO;
import aprimorar.atendimentos.api.dto.FinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AlunoAppointmentsResponseDTO;
import aprimorar.atendimentos.api.dto.AlunoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AlunosFinanceSummaryResponseDTO;
import aprimorar.atendimentos.api.dto.AlunosWithFinanceResponseDTO;
import aprimorar.atendimentos.api.dto.AlunoSummaryDTO;
import aprimorar.atendimentos.api.dto.AlunoWithFinanceDTO;
import aprimorar.atendimentos.api.exception.AtendimentoNotFoundException;
import aprimorar.atendimentos.api.exception.AtendimentoConflitoAgendaException;
import aprimorar.atendimentos.api.exception.InvalidAtendimentoException;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository.TipoAtendimentoCount;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository.ColaboradorFinanceSummaryProjection;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository.AlunoFinanceSummaryProjection;
import aprimorar.atendimentos.internal.repository.AtendimentoSpecifications;
import aprimorar.financeiro.api.FinanceiroService;

import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.shared.PageDTO;

@Service
public class AtendimentoServiceImpl implements AtendimentoService, AtendimentoManagementService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoServiceImpl.class);

    private final AtendimentoRepository appointmentRepo;
    private final AtendimentoMapper appointmentMapper;
    private final FinanceiroService expenseService;
    private final AlunoService studentService;
    private final ColaboradorReadApi colaboradorService;
    private final Clock clock;

    public AtendimentoServiceImpl(
        AtendimentoRepository appointmentRepo,
        AtendimentoMapper appointmentMapper,
        FinanceiroService expenseService,
        AlunoService studentService,
        ColaboradorReadApi colaboradorService,
        Clock clock
    ) {
        this.appointmentRepo = appointmentRepo;
        this.appointmentMapper = appointmentMapper;
        this.expenseService = expenseService;
        this.studentService = studentService;
        this.colaboradorService = colaboradorService;
        this.clock = clock;
    }

    @Transactional
    public AtendimentoResponseDTO createAppointment(AtendimentoRequestDTO dto) {
        AlunoResponseDTO student = studentService.findById(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorService.buscarPorId(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), null);

        Atendimento appointment = new Atendimento(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            student.id(),
            student.name(),
            employee.id(),
            employee.name(),
            Instant.now(clock)
        );
        Atendimento saved = appointmentRepo.save(appointment);
        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public PageDTO<AtendimentoResponseDTO> getAppointments(
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

        Page<Atendimento> appointmentPage = appointmentRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        log.info("Consulta de appointments finalizada, {} registros encontrados.", appointmentPage.getTotalElements());
        return new PageDTO<>(dtoPage);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponseDTO findById(UUID id) {
        Atendimento appointment = appointmentRepo.findById(id).orElseThrow(AtendimentoNotFoundException::new);
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
        colaboradorService.buscarPorId(employeeId);

        Boolean paidFilter = Boolean.TRUE.equals(hidePaid) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.withEmployeeId(employeeId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withEmployeePaid(paidFilter));

        Page<Atendimento> appointmentPage = appointmentRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        long totalEvents = appointmentRepo.countFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalPaid = appointmentRepo.sumPaidFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalUnpaid = appointmentRepo.sumUnpaidFilteredByEmployeeId(employeeId, startDate, endDate);

        log.info(
            "Consulta de appointments do colaborador finalizada, {} registros encontrados.",
            appointmentPage.getTotalElements()
        );

        return new ColaboradorAppointmentsResponseDTO(
            new PageDTO<>(dtoPage),
            new ColaboradorSummaryDTO(totalEvents, totalPaid, totalUnpaid)
        );
    }

    @Transactional(readOnly = true)
    public AtendimentoFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate) {
        BigDecimal totalStudentCharged = appointmentRepo.sumChargedFiltered(startDate, endDate);
        BigDecimal totalStudentPending = appointmentRepo.sumPendingFiltered(startDate, endDate);
        BigDecimal totalEmployeePaid = appointmentRepo.sumPaidFiltered(startDate, endDate);
        BigDecimal totalEmployeePending = appointmentRepo.sumUnpaidFiltered(startDate, endDate);
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

    @Transactional(readOnly = true)
    public AlunosFinanceSummaryResponseDTO getStudentsFinanceReport(Instant startDate, Instant endDate) {
        List<AlunoFinanceSummaryDTO> students = appointmentRepo.findAlunoFinanceSummaries(startDate, endDate).stream()
            .map(this::toStudentFinanceSummary)
            .toList();

        return new AlunosFinanceSummaryResponseDTO(startDate, endDate, students);
    }

    @Transactional(readOnly = true)
    public ColaboradoresFinanceSummaryResponseDTO getEmployeesFinanceReport(Instant startDate, Instant endDate) {
        List<ColaboradorFinanceSummaryDTO> employees = appointmentRepo.findColaboradorFinanceSummaries(startDate, endDate).stream()
            .map(this::toEmployeeFinanceSummary)
            .toList();

        return new ColaboradoresFinanceSummaryResponseDTO(startDate, endDate, employees);
    }

    @Transactional(readOnly = true)
    public ColaboradoresWithFinanceResponseDTO getEmployeesWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    ) {
        PageDTO<ColaboradorResponseDTO> employees = colaboradorService.getColaboradores(pageable, search, archived);
        List<ColaboradorFinanceSummaryDTO> financeRows = appointmentRepo.findColaboradorFinanceSummaries(startDate, endDate)
            .stream()
            .map(this::toEmployeeFinanceSummary)
            .toList();

        Map<UUID, ColaboradorFinanceSummaryDTO> financeByEmployeeId = financeRows.stream()
            .filter(employee -> employee.employeeId() != null)
            .collect(Collectors.toMap(ColaboradorFinanceSummaryDTO::employeeId, Function.identity()));

        List<ColaboradorWithFinanceDTO> content = employees.content().stream()
            .map(employee -> toEmployeeWithFinance(employee, financeByEmployeeId.get(employee.id())))
            .toList();

        FinanceSummaryDTO summary = summarizeEmployeeFinance(financeRows);

        return new ColaboradoresWithFinanceResponseDTO(
            employees.page(),
            employees.size(),
            employees.totalElements(),
            employees.totalPages(),
            content,
            summary
        );
    }

    @Transactional(readOnly = true)
    public AlunosWithFinanceResponseDTO getStudentsWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    ) {
        PageDTO<AlunoResponseDTO> students = studentService.getStudents(pageable, search, archived);
        List<AlunoFinanceSummaryDTO> financeRows = appointmentRepo.findAlunoFinanceSummaries(startDate, endDate)
            .stream()
            .map(this::toStudentFinanceSummary)
            .toList();

        Map<UUID, AlunoFinanceSummaryDTO> financeByStudentId = financeRows.stream()
            .filter(student -> student.studentId() != null)
            .collect(Collectors.toMap(AlunoFinanceSummaryDTO::studentId, Function.identity()));

        List<AlunoWithFinanceDTO> content = students.content().stream()
            .map(student -> toStudentWithFinance(student, financeByStudentId.get(student.id())))
            .toList();

        AlunoSummaryDTO summary = summarizeStudentFinance(financeRows);

        return new AlunosWithFinanceResponseDTO(
            students.page(),
            students.size(),
            students.totalElements(),
            students.totalPages(),
            content,
            summary
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
        studentService.findById(studentId);

        Specification<Atendimento> spec = AtendimentoSpecifications.withStudentId(studentId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withStudentCharged(charged));

        Page<Atendimento> appointmentPage = appointmentRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

        log.info("Consulta de appointments do aluno finalizada, {} registros encontrados.", appointmentPage.getTotalElements());

        long totalEvents = appointmentRepo.countFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalCharged = appointmentRepo.sumChargedFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalPending = appointmentRepo.sumPendingFilteredByStudentId(studentId, startDate, endDate);

        return new AlunoAppointmentsResponseDTO(
            new PageDTO<>(dtoPage),
            new AlunoSummaryDTO(totalEvents, totalCharged, totalPending)
        );
    }

    @Transactional
    public AtendimentoResponseDTO updateAppointment(UUID id, AtendimentoRequestDTO dto) {
        Atendimento appointment = findAppointmentOrThrow(id);
        AlunoResponseDTO student = studentService.findById(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorService.buscarPorId(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), appointment);

        appointment.update(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            student.id(),
            student.name(),
            employee.id(),
            employee.name(),
            Instant.now(clock)
        );
        log.info("Atendimento {} atualizado com sucesso.", appointment.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        Atendimento found = findAppointmentOrThrow(id);
        appointmentRepo.delete(found);
        log.info("Atendimento {} deletado com sucesso.", found.getTitle().toUpperCase());
    }

    @Transactional
    public AtendimentoResponseDTO toggleStudentCharge(UUID id) {
        Atendimento appointment = findAppointmentOrThrow(id);
        appointment.toggleStudentCharge(Instant.now(clock));
        log.info("Status da cobrança do aluno no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public AtendimentoResponseDTO toggleEmployeePayment(UUID id) {
        Atendimento appointment = findAppointmentOrThrow(id);
        appointment.toggleEmployeePayment(Instant.now(clock));
        log.info("Status do pagamento do colaborador no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public void reassignStudentAppointmentsToGhost(UUID studentId) {
        appointmentRepo.reassignStudentAppointmentsToGhost(studentId, UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Transactional
    public void reassignEmployeeAppointmentsToGhost(UUID employeeId) {
        appointmentRepo.reassignEmployeeAppointmentsToGhost(employeeId, UUID.fromString("00000000-0000-4000-8000-000000000001"));
    }

    @Transactional(readOnly = true)
    public long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId) {
        return appointmentRepo.countDistinctStudentsInPeriodExcludingAluno(startDate, endDate, excludedStudentId);
    }

    @Transactional(readOnly = true)
    public long countAppointmentsInPeriod(Instant startDate, Instant endDate) {
        return appointmentRepo.countByStartDateGreaterThanEqualAndStartDateLessThan(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate) {
        List<TipoAtendimentoCount> distribution = appointmentRepo.findContentDistributionInPeriod(startDate, endDate);
        long total = distribution.stream().mapToLong(TipoAtendimentoCount::getCount).sum();
        return distribution.stream()
            .map(p -> new ContentDistributionDTO(
                p.getContent().name(),
                p.getCount(),
                total > 0
                    ? BigDecimal.valueOf(p.getCount() * 100.0 / total).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO
            ))
            .toList();
    }

    /* ----- Helper Methods ----- */
    private Atendimento findAppointmentOrThrow(UUID id) {
        return appointmentRepo.findById(id).orElseThrow(AtendimentoNotFoundException::new);
    }

    private void validateParticipantAvailability(
        AlunoResponseDTO student,
        ColaboradorResponseDTO employee,
        Instant startDate,
        Double duration,
        Atendimento appointment
    ) {
        Instant endDate = Atendimento.calculateEndDate(startDate, duration);

        if (!student.active()) {
            throw new InvalidAtendimentoException("Atendimento nao pode ter estudantes arquivados");
        }

        if (!employee.active()) {
            throw new InvalidAtendimentoException("Atendimento nao pode ter colaboradores arquivados");
        }

        UUID appointmentId = appointment != null ? appointment.getId() : null;

        if (appointmentRepo.studentHasConflictingAppointment(student.id(), startDate, endDate, appointmentId)) {
            throw new AtendimentoConflitoAgendaException("O estudante informado ja possui um appointment no intervalo");
        }

        if (appointmentRepo.employeeHasConflictingAppointment(employee.id(), startDate, endDate, appointmentId)) {
            throw new AtendimentoConflitoAgendaException("O colaborador informado ja possui um appointment no intervalo");
        }
    }

    private AlunoFinanceSummaryDTO toStudentFinanceSummary(AlunoFinanceSummaryProjection projection) {
        return new AlunoFinanceSummaryDTO(
            projection.getStudentId(),
            projection.getStudentName(),
            projection.getTotalEvents(),
            projection.getTotalCharged(),
            projection.getTotalPending()
        );
    }

    private ColaboradorFinanceSummaryDTO toEmployeeFinanceSummary(ColaboradorFinanceSummaryProjection projection) {
        return new ColaboradorFinanceSummaryDTO(
            projection.getEmployeeId(),
            projection.getEmployeeName(),
            projection.getTotalEvents(),
            projection.getTotalPaid(),
            projection.getTotalPending()
        );
    }

    private ColaboradorWithFinanceDTO toEmployeeWithFinance(
        ColaboradorResponseDTO employee,
        ColaboradorFinanceSummaryDTO finance
    ) {
        return new ColaboradorWithFinanceDTO(
            employee.id(),
            employee.name(),
            employee.cpf(),
            employee.contact(),
            employee.duty(),
            employee.createdAt(),
            employee.active(),
            finance != null ? finance.totalPaid() : BigDecimal.ZERO,
            finance != null ? finance.totalPending() : BigDecimal.ZERO
        );
    }

    private FinanceSummaryDTO summarizeEmployeeFinance(List<ColaboradorFinanceSummaryDTO> financeRows) {
        long totalEvents = 0;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;

        for (ColaboradorFinanceSummaryDTO employee : financeRows) {
            totalEvents += employee.totalEvents();
            totalPaid = totalPaid.add(employee.totalPaid());
            totalPending = totalPending.add(employee.totalPending());
        }

        return new FinanceSummaryDTO(totalEvents, totalPaid, totalPending);
    }

    private AlunoWithFinanceDTO toStudentWithFinance(
        AlunoResponseDTO student,
        AlunoFinanceSummaryDTO finance
    ) {
        return new AlunoWithFinanceDTO(
            student.id(),
            student.name(),
            student.cpf(),
            student.contact(),
            student.age(),
            student.school(),
            student.createdAt(),
            student.active(),
            finance != null ? finance.totalCharged() : BigDecimal.ZERO,
            finance != null ? finance.totalPending() : BigDecimal.ZERO
        );
    }

    private AlunoSummaryDTO summarizeStudentFinance(List<AlunoFinanceSummaryDTO> financeRows) {
        long totalEvents = 0;
        BigDecimal totalCharged = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;

        for (AlunoFinanceSummaryDTO student : financeRows) {
            totalEvents += student.totalEvents();
            totalCharged = totalCharged.add(student.totalCharged());
            totalPending = totalPending.add(student.totalPending());
        }

        return new AlunoSummaryDTO(totalEvents, totalCharged, totalPending);
    }
}
