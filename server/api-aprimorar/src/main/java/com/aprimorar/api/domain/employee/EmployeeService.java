package com.aprimorar.api.domain.employee;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.entity.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final Clock applicationClock;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper, Clock applicationClock) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.applicationClock = applicationClock;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listEmployees(Pageable pageable, boolean includeArchived) {
        Page<Employee> employeePage = employeeRepo.findAll(pageable);
        return employeePage.map(employeeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        EmployeeResponseDTO employeeResponseDTO;
        try {
            log.info("ProductService:getProductById execution started");
            
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Funcionário com o ID não encontrado:" + employeeId));
            employeeResponseDTO = employeeMapper.toDto(employee);

            log.debug("EmployeeService:getProductById retrieving product from database for id {} {}", employee, employeeMapper.jsonAsString(employeeResponseDTO));

        } catch (Exception ex) {
            log.info("Ocorreu um erro ao buscar o funcionário {} do banco de dados. Mensagem de erro: {}", employeeId, ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar produtos no Banco de Dados " + employeeId);
        }

        log.info("EmployeeService:getProductById execution ended");
        return employeeResponseDTO;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) {
        log.info("Creating employee");
        Employee newEmployee = employeeMapper.toEntity(employeeRequestDto);
        Employee savedEmployee = employeeRepo.save(newEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Transactional
    public void softDeleteEmployee(UUID employeeId) {
        Employee foundEmployee = findAnyEmployeeOrThrow(employeeId);
        log.info("Deactivating employeeId={}", employeeId);
        deactivateIfActive(foundEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee foundEmployee = findAnyEmployeeOrThrow(employeeId);
        log.info("Updating employeeId={}", employeeId);

        employeeMapper.updateFromDto(updateEmployeeDto, foundEmployee);
        foundEmployee.setUpdatedAt(Instant.now(applicationClock));

        return employeeMapper.toDto(foundEmployee);
    }

    private Employee findAnyEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Mudar esse erro"));
    }

    private void deactivateIfActive(Employee foundEmployee) {
        if (foundEmployee.getArchivedAt() == null) {
            Instant now = Instant.now(applicationClock);
            foundEmployee.setArchivedAt(now);
            foundEmployee.setUpdatedAt(now);
        }
    }
}
