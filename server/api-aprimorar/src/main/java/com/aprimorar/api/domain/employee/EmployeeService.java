package com.aprimorar.api.domain.employee;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
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

        Page<Employee> page = employeeRepo.findAll(pageable);
        return page.map(employeeMapper::convertToDto);
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

        EmployeeRules.validate(employee);
        validateEmployeeUniqueness(employee.getCpf(), employee.getEmail());

        Employee savedEmployee = employeeRepo.save(employee);

        return employeeMapper.convertToDto(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request) {

        Employee employee = employeeMapper.convertToEntity(request);

        EmployeeRules.validate(employee);
        findEmployeeOrThrow(employeeId);

        Employee updatedEmployee = employeeRepo.save(employee);

        return employeeMapper.convertToDto(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employeeRepo.delete(employee);
    }

    @Transactional
    public void archiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.setArchivedAt(Instant.now());
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        employee.setArchivedAt(null);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Colaborador não encontrado no Banco de Dados"));
    }

    private void validateEmployeeUniqueness(String cpf, String email) {
        employeeRepo.finByCpf(cpf)
                .orElseThrow(()-> new EmployeeAlreadyExistsException("Colaborador com o CPF informado já cadastrado no banco de dados"));

        employeeRepo.findByEmail(email)
                .orElseThrow(()-> new EmployeeAlreadyExistsException("Colaborador com o Email informado já cadastrado no banco de dados"));

    }
}
