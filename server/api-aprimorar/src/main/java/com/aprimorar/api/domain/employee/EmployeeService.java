package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.employee.dto.EmployeeOptionsDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.employee.repository.EmployeeSpecifications;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.PageDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private static final UUID PHANTOM_EMPLOYEE_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");
    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final EventRepository eventRepo;

    public EmployeeService(
        EmployeeRepository employeeRepo,
        EmployeeMapper employeeMapper,
        EventRepository eventRepo
    ) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.eventRepo = eventRepo;
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search) {
        Specification<Employee> spec = (root, query, cb) -> cb.notEqual(root.get("duty"), Duty.SYSTEM);

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(EmployeeSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Employee> employeePage = employeeRepo.findAll(spec, pageable);
        Page<EmployeeResponseDTO> parentsDtoPage = employeePage.map(employeeMapper::convertToDto);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", employeePage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<EmployeeOptionsDTO> getEmployeeOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return employeeRepo
            .findAll(EmployeeSpecifications.notArchived(), sort)
            .stream()
            .map(e -> new EmployeeOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        log.info("Colaborador {} consultado com sucesso.", employee.getName().toUpperCase());
        return employeeMapper.convertToDto(employee);
    }

    /* ----- Command Methods ----- */
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) {
        Employee employee = employeeMapper.convertToEntity(employeeRequestDto);

        ensureEmployeeUniqueness(employee.getCpf(), employee.getEmail());

        Employee savedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} cadastrado com sucesso.", savedEmployee.getName().toUpperCase());
        return employeeMapper.convertToDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request) {
        
        Employee employee = findEmployeeOrThrow(employeeId);
        Employee updatedEmployeeData = employeeMapper.convertToEntity(request);

        ensureEmployeeUniquenessForUpdate(updatedEmployeeData, employeeId);

        employee.setName(updatedEmployeeData.getName());
        employee.setBirthdate(updatedEmployeeData.getBirthdate());
        employee.setPix(updatedEmployeeData.getPix());
        employee.setContact(updatedEmployeeData.getContact());
        employee.setEmail(updatedEmployeeData.getEmail());
        employee.setDuty(updatedEmployeeData.getDuty());

        log.info("Colaborador {} atualizado com sucesso.", employee.getName().toUpperCase());
        return employeeMapper.convertToDto(employee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        eventRepo.reassignEmployeeEventsToGhost(employeeId, PHANTOM_EMPLOYEE_ID);
        employeeRepo.delete(employee);
        log.info("Colaborador {} deletado com sucesso. Eventos transferidos para arquivo morto fantasma.", employee.getName().toUpperCase());
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
        return employeeRepo.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no Banco de Dados"));
    }

    private void ensureEmployeeUniqueness(String cpf, String email) {
        if (employeeRepo.existsByCpf(cpf)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o CPF informado já cadastrado no banco de dados");
        }

        if (employeeRepo.existsByEmail(email)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados");
        }
    }

    private void ensureEmployeeUniquenessForUpdate(Employee employee, UUID employeeId) {
        if (employeeRepo.existsByCpfAndIdNot(employee.getCpf(), employeeId)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o CPF informado já cadastrado no banco de dados");
        }

        if (employeeRepo.existsByEmailAndIdNot(employee.getEmail(), employeeId)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados");
        }
    }
}
