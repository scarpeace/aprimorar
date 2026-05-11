package aprimorar.appointment.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.registration.employee.api.EmployeeService;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.appointment.api.AppointmentService;
import aprimorar.appointment.api.dto.ContentDistributionDTO;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.appointment.api.exception.AppointmentNotFoundException;
import aprimorar.appointment.api.exception.AppointmentScheduleConflictException;
import aprimorar.appointment.api.exception.InvalidAppointmentException;
import aprimorar.appointment.internal.repository.AppointmentRepository;
import aprimorar.appointment.internal.repository.AppointmentRepository.AppointmentContentCount;
import aprimorar.finance.api.TransactionService;
import aprimorar.registration.student.api.StudentService;
import aprimorar.registration.student.api.dto.StudentResponseDTO;
import aprimorar.shared.PageDTO;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepo;
    private final AppointmentMapper appointmentMapper;
    private final TransactionService transactionService;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final Clock clock;

    public AppointmentServiceImpl(
        AppointmentRepository appointmentRepo,
        AppointmentMapper appointmentMapper,
        TransactionService transactionService,
        StudentService studentService,
        EmployeeService employeeService,
        Clock clock
    ) {
        this.appointmentRepo = appointmentRepo;
        this.appointmentMapper = appointmentMapper;
        this.transactionService = transactionService;
        this.studentService = studentService;
        this.employeeService = employeeService;
        this.clock = clock;
    }

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
        StudentResponseDTO student = studentService.findById(dto.studentId());
        EmployeeResponseDTO employee = employeeService.findById(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), null);

        Appointment appointment = new Appointment(
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
        Appointment saved = appointmentRepo.save(appointment);
        transactionService.createAppointmentTransactions(saved.getId(), saved.getPrice(), saved.getPayment());
        log.info("Appointment {} cadastrado com sucesso.", saved.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(saved);
    }

   @Transactional(readOnly = true)
   public PageDTO<AppointmentResponseDTO> getAppointments(Pageable pageable){

       Page<Appointment> appointmentPage = appointmentRepo.findAll(pageable);
       Page<AppointmentResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);

       log.info("Consulta de appointments finalizada, {} registros encontrados.", appointmentPage.getTotalElements());
       return new PageDTO<>(dtoPage);
   }

    @Transactional(readOnly = true)
    public AppointmentResponseDTO findById(UUID id) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(AppointmentNotFoundException::new);
        log.info("Appointment {} consultado com sucesso.", appointment.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(appointment);
    }

   @Transactional(readOnly = true)
   public PageDTO<AppointmentResponseDTO> getAppointmentsByEmployeeId(
       Pageable pageable,
       UUID employeeId
   ) {
       Page<Appointment> appointmentPage = appointmentRepo.findAllByEmployeeId(employeeId, pageable);
       Page<AppointmentResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);
       log.info("Consulta de appointments do colaborador finalizada, {} registros encontrados.", appointmentPage.getTotalElements());

       return new PageDTO<>(dtoPage);
   }

   @Transactional(readOnly = true)
   public PageDTO<AppointmentResponseDTO> getAppointmentsByStudentId(
       Pageable pageable,
       UUID studentId
   ) {
       Page<Appointment> appointmentPage = appointmentRepo.findAllByStudentId(studentId, pageable);
       Page<AppointmentResponseDTO> dtoPage = appointmentPage.map(appointmentMapper::convertToDto);
       log.info("Consulta de appointments do aluno finalizada, {} registros encontrados.", appointmentPage.getTotalElements());

       return new PageDTO<>(dtoPage);
   }

    @Transactional
    public AppointmentResponseDTO updateAppointment(UUID id, AppointmentRequestDTO dto) {
        Appointment appointment = findAppointmentOrThrow(id);
        StudentResponseDTO student = studentService.findById(dto.studentId());
        EmployeeResponseDTO employee = employeeService.findById(dto.employeeId());

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
        transactionService.syncAppointmentTransactions(appointment.getId(), appointment.getPrice(), appointment.getPayment());

        log.info("Appointment {} atualizado com sucesso.", appointment.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        Appointment found = findAppointmentOrThrow(id);
        appointmentRepo.delete(found);
        log.info("Appointment {} deletado com sucesso.", found.getTitle().toUpperCase());
    }

    @Transactional
    public AppointmentResponseDTO toggleStudentCharge(UUID id) {
        Appointment appointment = findAppointmentOrThrow(id);
        appointment.toggleStudentCharge(Instant.now(clock));
        transactionService.syncStudentCharge(appointment.getId(), appointment.getStudentChargeDate());
        log.info("Status da cobrança do aluno no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public AppointmentResponseDTO toggleEmployeePayment(UUID id) {
        Appointment appointment = findAppointmentOrThrow(id);
        appointment.toggleEmployeePayment(Instant.now(clock));
        transactionService.syncEmployeePayment(appointment.getId(), appointment.getEmployeePaymentDate());
        log.info("Status do pagamento do colaborador no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional(readOnly = true)
    public long countByStudentId(UUID studentId) {
        return appointmentRepo.countByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public long countByEmployeeId(UUID employeeId) {
        return appointmentRepo.countByEmployeeId(employeeId);
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
        return appointmentRepo.countDistinctStudentsInPeriodExcludingStudent(startDate, endDate, excludedStudentId);
    }

    @Transactional(readOnly = true)
    public long countAppointmentsInPeriod(Instant startDate, Instant endDate) {
        return appointmentRepo.countByStartDateGreaterThanEqualAndStartDateLessThan(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate) {
        List<AppointmentContentCount> distribution = appointmentRepo.findContentDistributionInPeriod(startDate, endDate);
        long total = distribution.stream().mapToLong(AppointmentContentCount::getCount).sum();
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
    private Appointment findAppointmentOrThrow(UUID id) {
        return appointmentRepo.findById(id).orElseThrow(AppointmentNotFoundException::new);
    }

    private void validateParticipantAvailability(
        StudentResponseDTO student,
        EmployeeResponseDTO employee,
        Instant startDate,
        Double duration,
        Appointment appointment
    ) {
        Instant endDate = Appointment.calculateEndDate(startDate, duration);

        if (!student.active()) {
            throw new InvalidAppointmentException("Appointment nao pode ter estudantes arquivados");
        }

        if (!employee.active()) {
            throw new InvalidAppointmentException("Appointment nao pode ter colaboradores arquivados");
        }

        UUID appointmentId = appointment != null ? appointment.getId() : null;

        if (appointmentRepo.studentHasConflictingAppointment(student.id(), startDate, endDate, appointmentId)) {
            throw new AppointmentScheduleConflictException("O estudante informado ja possui um appointment no intervalo");
        }

        if (appointmentRepo.employeeHasConflictingAppointment(employee.id(), startDate, endDate, appointmentId)) {
            throw new AppointmentScheduleConflictException("O colaborador informado ja possui um appointment no intervalo");
        }
    }
}
