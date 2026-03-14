package com.aprimorar.api.domain.employee;

import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;

/**
 * Centraliza as regras de negócio do colaborador.
 *
 * <p>Aqui ficam a criação, atualização, consultas e ações de arquivar/desarquivar.
 * Também é esse service que garante que CPF e email não se repitam antes de salvar
 * ou atualizar um colaborador.
 *
 * <p>Quando um colaborador é alterado, o service aplica as validações do domínio,
 * resolve conflitos de unicidade e devolve a resposta em formato DTO.
 *
 * @author scarpellini
 * @version 1.0
 * @since 2026-03-14
 */
@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    /* ----- Query Methods ----- */

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(Pageable pageable) {

        Page<Employee> page = employeeRepo.findAll(pageable);
        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(employeeMapper::convertToDto);
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

        EmployeeRules.validate(employee);
        validateEmployeeUniqueness(employee.getCpf(), employee.getEmail());

        Employee savedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} cadastrado com sucesso.", savedEmployee.getName().toUpperCase());
        return employeeMapper.convertToDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request) {

        Employee employee = employeeMapper.convertToEntity(request);

        EmployeeRules.validate(employee);
        findEmployeeOrThrow(employeeId);

        Employee updatedEmployee = employeeRepo.save(employee);

        log.info("Colaborador {} atualizado com sucesso.", updatedEmployee.getName().toUpperCase());
        return employeeMapper.convertToDto(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employeeRepo.delete(employee);
        log.info("Colaborador {} deletado com sucesso.", employee.getName().toUpperCase());
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
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no Banco de Dados"));
    }

    private void validateEmployeeUniqueness(String cpf, String email) {
        if (employeeRepo.existsByCpf(cpf)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o CPF informado já cadastrado no banco de dados");
        }

        if (employeeRepo.existsByEmail(email)) {
            throw new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados");
        }

    }
}
