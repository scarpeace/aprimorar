package aprimorar.registration.employee.internal;

import aprimorar.registration.api.exception.PersonHasPendingFinancialsException;
import aprimorar.registration.employee.api.EmployeeService;
import aprimorar.registration.employee.api.dto.EmployeeOptionsDTO;
import aprimorar.registration.employee.api.dto.EmployeeRequestDTO;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.registration.employee.api.event.EmployeeDeletedEvent;
import aprimorar.registration.employee.api.exception.EmployeeNotFoundException;
import aprimorar.registration.employee.internal.repository.EmployeeRepository;
import aprimorar.registration.employee.internal.repository.EmployeeSpecifications;
import aprimorar.registration.shared.address.Address;
import aprimorar.registration.shared.PendingFinancialBalanceChecker;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.PageDTO;

import java.util.*;
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
    private static final UUID GHOST_EMPLOYEE_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");

    private final EmployeeRepository employeeRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final PendingFinancialBalanceChecker pendingFinancialBalanceChecker;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepo,
        ApplicationEventPublisher eventPublisher,
        PendingFinancialBalanceChecker pendingFinancialBalanceChecker
    ) {
        this.employeeRepo = employeeRepo;
        this.eventPublisher = eventPublisher;
        this.pendingFinancialBalanceChecker = pendingFinancialBalanceChecker;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        Address address = Address.fromRequest(dto.address());

        Employee employee = new Employee(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.cpf(),
            dto.email(),
            dto.duty(),
            address
        );

        Employee savedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} cadastrado com sucesso.", savedEmployee.getName().toUpperCase());
        return savedEmployee.toResponseDto();
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
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        log.info("Colaborador {} consultado com sucesso.", employee.getName().toUpperCase());
        return employee.toResponseDto();
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID employeeId) {
        if(!employeeRepo.existsById(employeeId)){
            throw new EmployeeNotFoundException("Colaborador não encontrado no banco de dados");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getReferenceById(UUID id) {
        return findEmployeeOrThrow(id).toResponseDto();
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request) {
        if (GHOST_EMPLOYEE_ID.equals(employeeId)) {
            throw new IllegalArgumentException("Nao e possivel modificar o registro de sistema 'Colaborador Removido'.");
        }

        Employee employee = findEmployeeOrThrow(employeeId);

        String normalizedContact = MapperUtils.normalizeContact(request.contact());
        String normalizedEmail = MapperUtils.normalizeEmail(request.email());
        Address address = Address.fromRequest(request.address());

        employee.updateDetails(request.name(), request.birthdate(), request.pix(), normalizedContact, normalizedEmail, request.duty(), address);

        log.info("Colaborador {} atualizado com sucesso.", employee.getName().toUpperCase());
        return employee.toResponseDto();
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        if (GHOST_EMPLOYEE_ID.equals(employeeId)) {
            throw new IllegalArgumentException("Nao e possivel deletar o registro de sistema 'Colaborador Removido'.");
        }

        Employee employee = findEmployeeOrThrow(employeeId);

        if (pendingFinancialBalanceChecker.hasPendingEmployeePayments(employeeId)) {
            throw new PersonHasPendingFinancialsException(
                "O colaborador possui pagamentos pendentes. Quite os valores antes de excluí-lo."
            );
        }

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
        if (GHOST_EMPLOYEE_ID.equals(employeeId)) {
            throw new IllegalArgumentException("Nao e possivel arquivar o registro de sistema 'Colaborador Removido'.");
        }

        Employee employee = findEmployeeOrThrow(employeeId);
        employee.archive();
        log.info("Colaborador {} arquivado com sucesso.", employee.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        if (GHOST_EMPLOYEE_ID.equals(employeeId)) {
            throw new IllegalArgumentException("O registro 'Colaborador Removido' nao pode ser desarquivado.");
        }

        Employee employee = findEmployeeOrThrow(employeeId);
        employee.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", employee.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no Banco de Dados"));
    }
}
