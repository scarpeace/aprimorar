package com.aprimorar.api.service;

import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.mapper.EmployeeMapper;
import com.aprimorar.api.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }

    public Page<EmployeeResponseDTO> listEmployees(Pageable pageable) {
        Page<Employee> employeePage =  employeeRepo.findAll(pageable);
        return employeePage.map(employeeMapper::toDto);
    }
}
