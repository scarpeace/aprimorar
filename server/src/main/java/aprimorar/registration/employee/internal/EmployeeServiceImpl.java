package aprimorar.registration.employee.internal;

import aprimorar.appointment.api.AppointmentService;
import aprimorar.registration.api.exception.PersonHasPendingFinancialsException;
import aprimorar.registration.employee.api.Duty;
import aprimorar.registration.employee.api.EmployeeService;
import aprimorar.registration.employee.api.dto.EmployeeCountSummaryDTO;
import aprimorar.registration.employee.api.dto.EmployeeOptionsDTO;
import aprimorar.registration.employee.api.dto.EmployeeRequestDTO;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.registration.employee.api.event.EmployeeDeletedEvent;
import aprimorar.registration.employee.api.exception.EmployeeBusinessException;
import aprimorar.registration.employee.api.exception.EmployeeNotFoundException;
import aprimorar.registration.employee.internal.repository.EmployeeRepository;
import aprimorar.registration.employee.internal.repository.EmployeeSpecifications;
import aprimorar.registration.shared.address.AddressMapper;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final AppointmentService appointmentService;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepo,
        ApplicationEventPublisher eventPublisher,
        AppointmentService appointmentService
    ) {
        this.employeeRepo = employeeRepo;
        this.eventPublisher = eventPublisher;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepo.existsByCpf(MapperUtils.normalizeCpf(dto.cpf()))) {
            throw new EmployeeBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (employeeRepo.existsByEmail(MapperUtils.normalizeEmail(dto.email()))) {
            throw new EmployeeBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        var employee = EmployeeMapper.toEntity(dto);
        Employee savedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} cadastrado com sucesso.", savedEmployee.getName().toUpperCase());
        return EmployeeMapper.toDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    public PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived) {
        Specification<Employee> spec = EmployeeSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(EmployeeSpecifications.isArchived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(EmployeeSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Employee> employeePage = employeeRepo.findAll(spec, pageable);
        Page<EmployeeResponseDTO> employeesDtoPage = employeePage.map(EmployeeMapper::toDto);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", employeePage.getTotalElements());
        return new PageDTO<>(employeesDtoPage);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        log.info("Colaborador {} consultado com sucesso.", employee.getName().toUpperCase());
        return EmployeeMapper.toDto(employee);
    }

    //TODO: talvez dê para remover esse método e colocar essa countByDutyNotAndActiveTrue em algum outro lugar já que o total de employees não interessa.
    @Transactional(readOnly = true)
    public EmployeeCountSummaryDTO getSummary() {
        long activeEmployees = employeeRepo.countByDutyNotAndActiveTrue(Duty.SYSTEM);
        long totalEmployees = employeeRepo.countByDutyNot(Duty.SYSTEM);

        return new EmployeeCountSummaryDTO(activeEmployees, totalEmployees);
    }

    @Transactional(readOnly = true)
    public List<EmployeeOptionsDTO> getEmployeeOptions() {
        return employeeRepo
            .findAllByDutyNotAndActiveTrueOrderByNameAsc(Duty.SYSTEM)
            .stream()
            .map(e -> new EmployeeOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO dto) {
        if (employeeRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), employeeId)) {
            throw new EmployeeBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
        }

        var employee = employeeRepo
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeBusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado."));

        ensureNotSystem(employee, "Não é possível modificar o registro de sistema 'Colaborador Removido'.");

        employee.update(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.email(),
            dto.duty(),
            AddressMapper.toEntity(dto.address())
        );

        log.info("Colaborador {} atualizado com sucesso.", employee.getName().toUpperCase());
        return EmployeeMapper.toDto(employee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        ensureNotSystem(employee, "Não é possível deletar o registro de sistema 'Colaborador Removido'.");

        if (appointmentService.hasPendingEmployeePayments(employeeId)) {
            throw new EmployeeBusinessException(HttpStatus.BAD_REQUEST,"O colaborador possui pagamentos pendentes. Quite os valores antes de excluí-lo.");
        }

        eventPublisher.publishEvent(new EmployeeDeletedEvent(employeeId));
        employeeRepo.delete(employee);

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            employee.getName().toUpperCase()
        );
    }

    @Transactional
    public void archiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);

        if (appointmentService.hasPendingEmployeePayments(employeeId)) {
            throw new EmployeeBusinessException(HttpStatus.BAD_REQUEST,"O colaborador possui pagamentos pendentes. Quite os valores antes de arquivar.");
        }

        ensureNotSystem(employee, "Não é possível arquivar o registro de sistema 'Colaborador Removido'.");
        employee.archive();

        log.info("Colaborador {} arquivado com sucesso.", employee.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        ensureNotSystem(employee, "O registro 'Colaborador Removido' não pode ser desarquivado.");
        employee.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", employee.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no banco de dados"));
    }

    private void ensureNotSystem(Employee employee, String message) {
        if (Duty.SYSTEM.equals(employee.getDuty())) {
            throw new EmployeeBusinessException(HttpStatus.CONFLICT, message);
        }
    }
}
