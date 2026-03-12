package com.aprimorar.api.domain.employee;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepo.findAll(pageable);

        return employeePage.map(employeeMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);

        return employeeMapper.convertToDto(employee);
    }

    /*
      ------------------------ INSERT METHODS ------------------------
     */

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) {
        Employee employee = employeeMapper.convertToEntity(employeeRequestDto);
        Employee savedEmployee = employeeRepo.save(employee);

        return employeeMapper.convertToDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee foundEmployee = findEmployeeOrThrow(employeeId);

        foundEmployee.setName(updateEmployeeDto.name());
        foundEmployee.setEmail(updateEmployeeDto.email());
        foundEmployee.setBirthdate(updateEmployeeDto.birthdate());
        foundEmployee.setPix(updateEmployeeDto.pix());
        foundEmployee.setContact(updateEmployeeDto.contact());
        foundEmployee.setCpf(updateEmployeeDto.cpf());

        return employeeMapper.convertToDto(foundEmployee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employeeRepo.delete(employee);
    }

    @Transactional
    public void archiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.archive();
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.unarchive();
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }
}
