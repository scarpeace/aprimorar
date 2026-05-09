package com.aprimorar.api.domain.registration.employee.internal;

import com.aprimorar.api.domain.registration.employee.api.EmployeeService;
import com.aprimorar.api.domain.registration.employee.api.dto.EmployeeSummaryDTO;
import com.aprimorar.api.domain.registration.employee.api.dto.EmployeeOptionsDTO;
import com.aprimorar.api.domain.registration.employee.api.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.registration.employee.api.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.registration.employee.api.event.EmployeeDeletedEvent;
import com.aprimorar.api.domain.registration.employee.api.exception.EmployeeAlreadyExistsException;
import com.aprimorar.api.domain.registration.employee.api.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.registration.employee.internal.repository.EmployeeRepository;
import com.aprimorar.api.domain.registration.employee.internal.repository.EmployeeSpecifications;
import com.aprimorar.api.domain.event.api.EventService;
import com.aprimorar.api.shared.MapperUtils;
import com.aprimorar.api.shared.PageDTO;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private static final UUID PHANTOM_EMPLOYEE_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");
    private final EmployeeRepository employeeRepo;
    private final EventService eventService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepo,
        EventService eventService,
        ApplicationEventPublisher eventPublisher,
        Clock clock
    ) {
        this.employeeRepo = employeeRepo;
        this.eventService = eventService;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) {
        String normalizedContact = MapperUtils.normalizeContact(employeeRequestDto.contact());
        String normalizedCpf = MapperUtils.normalizeCpf(employeeRequestDto.cpf());
        String normalizedEmail = MapperUtils.normalizeEmail(employeeRequestDto.email());

        Employee employee = new Employee(
            employeeRequestDto.name(),
            employeeRequestDto.birthdate(),
            employeeRequestDto.pix(),
            normalizedContact,
            normalizedCpf,
            normalizedEmail,
            employeeRequestDto.duty()
        );

        ensureEmployeeUniqueness(employee.getCpf(), employee.getEmail());

        Employee savedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} cadastrado com sucesso.", savedEmployee.getName().toUpperCase());
        return savedEmployee.toResponseDto();
    }

    @Transactional(readOnly = true)
    public PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived) {
        Specification<Employee> spec = EmployeeSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(EmployeeSpecifications.archived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(EmployeeSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Employee> employeePage = employeeRepo.findAll(spec, pageable);
        Page<EmployeeResponseDTO> parentsDtoPage = employeePage.map(Employee::toResponseDto);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", employeePage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<EmployeeOptionsDTO> getEmployeeOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        Specification<Employee> spec = EmployeeSpecifications.isNotGhost();

        return employeeRepo
            .findAll(spec, sort)
            .stream()
            .map(e -> new EmployeeOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<UUID> findIdsByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return employeeRepo.findByNameContainingIgnoreCase(name.trim())
            .stream()
            .map(Employee::getId)
            .toList();
    }

    @Transactional(readOnly = true)
    public java.util.Optional<UUID> findIdByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return java.util.Optional.empty();
        }
        return employeeRepo.findByEmail(MapperUtils.normalizeEmail(email))
            .map(Employee::getId);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        log.info("Colaborador {} consultado com sucesso.", employee.getName().toUpperCase());
        return employee.toResponseDto();
    }

    @Transactional(readOnly = true)
    public Map<UUID, EmployeeResponseDTO> findByIds(Collection<UUID> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Map.of();
        }
        return employeeRepo.findAllById(employeeIds)
            .stream()
            .collect(Collectors.toMap(Employee::getId, Employee::toResponseDto));
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return employeeRepo.existsById(id);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getReferenceById(UUID id) {
        return findEmployeeOrThrow(id).toResponseDto();
    }

    @Transactional(readOnly = true)
    public EmployeeSummaryDTO getSummary(UUID employeeId, Instant startDate, Instant endDate) {
        if (!employeeRepo.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Colaborador com o ID informado não encontrado");
        }

        if (startDate == null || endDate == null) {
            long totalEvents = eventService.countByEmployeeId(employeeId);
            BigDecimal totalPaid = eventService.sumPaidByEmployeeId(employeeId);
            BigDecimal totalUnpaid = eventService.sumUnpaidByEmployeeId(employeeId);

            log.info("Resumo geral gerado para o colaborador {}", employeeId);
            return new EmployeeSummaryDTO(totalEvents, totalPaid, totalUnpaid);
        }

        long totalEventsInPeriod = eventService.countByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
        BigDecimal totalPaidInPeriod = eventService.sumPaidByEmployeeIdInPeriod(employeeId, startDate, endDate);
        BigDecimal totalUnpaidInPeriod = eventService.sumUnpaidByEmployeeIdInPeriod(employeeId, startDate, endDate);

        log.info("Resumo gerado para o colaborador {} no período de {} a {}", employeeId, startDate, endDate);
        return new EmployeeSummaryDTO(totalEventsInPeriod, totalPaidInPeriod, totalUnpaidInPeriod);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request) {
        Employee employee = findEmployeeOrThrow(employeeId);

        String normalizedContact = MapperUtils.normalizeContact(request.contact());
        String normalizedEmail = MapperUtils.normalizeEmail(request.email());

        ensureEmployeeUniquenessForUpdate(normalizedEmail, employeeId);

        employee.updateDetails(request.name(), request.birthdate(), request.pix(), normalizedContact, normalizedEmail, request.duty());

        log.info("Colaborador {} atualizado com sucesso.", employee.getName().toUpperCase());
        return employee.toResponseDto();
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);

        log.info("Publicando evento de exclusão do colaborador {}.", employee.getName().toUpperCase());
        eventPublisher.publishEvent(new EmployeeDeletedEvent(employeeId));

        employeeRepo.delete(employee);
        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para arquivo morto fantasma.",
            employee.getName().toUpperCase()
        );
    }

    @Transactional
    public void archiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.setArchivedAt(Instant.now());
        log.info("Colaborador {} arquivado com sucesso.", employee.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.setArchivedAt(null);
        log.info("Colaborador {} desarquivado com sucesso.", employee.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no Banco de Dados"));
    }

    private void ensureEmployeeUniqueness(String cpf, String email) {
        if (employeeRepo.existsByCpf(cpf)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o CPF informado já cadastrado no banco de dados");
        }

        if (employeeRepo.existsByEmail(email)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados");
        }
    }

    private void ensureEmployeeUniquenessForUpdate(String email, UUID employeeId) {
        if (employeeRepo.existsByEmailAndIdNot(email, employeeId)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados");
        }
    }
}
