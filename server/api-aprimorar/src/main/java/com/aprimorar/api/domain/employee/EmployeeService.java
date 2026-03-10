package com.aprimorar.api.domain.employee;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.entity.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;
import com.aprimorar.api.shared.MapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final Clock applicationClock;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper, Clock applicationClock) {
        this.employeeRepo = employeeRepo;
        this.applicationClock = applicationClock;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) throws EmployeeServiceBusinessException {
        EmployeeResponseDTO response;
        
        try {
            log.info("EmployeeService:createEmployee execution started.");
            Employee employee = EmployeeMapper.convertToEntity(employeeRequestDto);
            log.debug("EmployeeService:createEmployee request parameters {}", MapperUtils.jsonAsString(employeeRequestDto));

            Employee savedEmployee = employeeRepo.save(employee);
            response = EmployeeMapper.convertToDto(savedEmployee);
            log.debug("EmployeeService:createEmployee received response from Database {}", MapperUtils.jsonAsString(response));

        } catch (Exception ex) {
            log.error("Exception occurred while persisting employee to database , Exception message {}", ex.getMessage());
            throw new EmployeeServiceBusinessException("Exception occurred while create a new employee");
        }
        log.info("EmployeeService:createEmployee execution ended.");
        return response;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(PageRequest pr) throws EmployeeServiceBusinessException {
       
        try {
            log.info("EmployeeService:getEmployees execution started");

            Page<Employee> employeePage = employeeRepo.findAll(pr);

            log.info("EmployeeService:getEmployees execution ended");
            return employeePage.map(EmployeeMapper::convertToDto);
        } catch (Exception ex) {
            log.info("Ocorreu um erro ao buscar os funcionários do banco de dados. Mensagem de erro: {}", ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar os funcionários do Banco de Dados");
        }

    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getById(UUID employeeId) {
        EmployeeResponseDTO employeeResponseDTO;
        try {
            log.info("ProductService:getProductById execution started");

            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Funcionário com o ID não encontrado:" + employeeId));
            employeeResponseDTO = EmployeeMapper.convertToDto(employee);

            log.debug("EmployeeService:getProductById retrieving product from database for id {} {}", employee, MapperUtils.jsonAsString(employeeResponseDTO));

        } catch (Exception ex) {
            log.info("Ocorreu um erro ao buscar o funcionário {} do banco de dados. Mensagem de erro: {}", employeeId, ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar produtos no Banco de Dados " + employeeId);
        }

        log.info("EmployeeService:getProductById execution ended");
        return employeeResponseDTO;
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

        EmployeeMapper.updateFromDto(updateEmployeeDto, foundEmployee);
        foundEmployee.setUpdatedAt(Instant.now(applicationClock));

        return EmployeeMapper.convertToDto(foundEmployee);
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
