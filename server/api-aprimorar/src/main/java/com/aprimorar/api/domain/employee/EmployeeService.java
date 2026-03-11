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
        Page<EmployeeEntity> employeePage = employeeRepo.findAll(pageable);

        return employeePage.map(employeeMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO findById(UUID employeeId) {
        EmployeeEntity employeeEntity = findEmployeeOrThrow(employeeId);

        return employeeMapper.convertToDto(employeeEntity);
    }

    /*
      ------------------------ INSERT METHODS METHODS ------------------------
     */

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) {
        EmployeeEntity employeeEntity = employeeMapper.convertToEntity(employeeRequestDto);
        EmployeeEntity savedEmployeeEntity = employeeRepo.save(employeeEntity);

        return employeeMapper.convertToDto(savedEmployeeEntity);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        EmployeeEntity foundEmployeeEntity = findEmployeeOrThrow(employeeId);

        foundEmployeeEntity.setName(updateEmployeeDto.name());
        foundEmployeeEntity.setEmail(updateEmployeeDto.email());
        foundEmployeeEntity.setBirthdate(updateEmployeeDto.birthdate());
        foundEmployeeEntity.setPix(updateEmployeeDto.pix());
        foundEmployeeEntity.setContact(updateEmployeeDto.contact());
        foundEmployeeEntity.setCpf(updateEmployeeDto.cpf());

        return employeeMapper.convertToDto(foundEmployeeEntity);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        EmployeeEntity employeeEntity = findEmployeeOrThrow(employeeId);
        employeeRepo.delete(employeeEntity);
    }

    @Transactional
    public void archiveEmployee(UUID employeeId) {
        EmployeeEntity employeeEntity = findEmployeeOrThrow(employeeId);
        employeeEntity.archive();
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) {
        EmployeeEntity employeeEntity = findEmployeeOrThrow(employeeId);
        employeeEntity.unarchive();
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private EmployeeEntity findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }
}
