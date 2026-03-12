package com.aprimorar.api.domain.employee;

import java.time.Clock;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.enums.Role;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class EmployeeMapper {

    private final Clock applicationClock;

    public EmployeeMapper(Clock applicationClock){
        this.applicationClock = applicationClock;
    }

    public Employee convertToEntity(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee();

        employee.setName(employeeRequestDTO.name());
        employee.setBirthdate(employeeRequestDTO.birthdate());
        employee.setPix(employeeRequestDTO.pix().trim());
        employee.setContact(MapperUtils.normalizeContact(employeeRequestDTO.contact()));
        employee.setCpf(MapperUtils.normalizeCpf(employeeRequestDTO.cpf()));
        employee.setEmail(employeeRequestDTO.email().trim());
        employee.setRole(Role.EMPLOYEE);
        //TODO Mover essa criação da data para a camada de serviço em todos os mappers
        employee.setCreatedAt(applicationClock.instant());
        return employee;
    }

    public EmployeeResponseDTO convertToDto(Employee entity) {

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPix(),
                MapperUtils.formatContact(entity.getContact()),
                MapperUtils.formatCpf(entity.getCpf()),
                entity.getEmail(),
                entity.getRole(),
                entity.getArchivedAt() == null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Employee updateToEntity(UpdateEmployeeDTO updateEmployeeDTO) {
        Employee updatedEmployee = new Employee();

        updatedEmployee.setName(updateEmployeeDTO.name());
        updatedEmployee.setBirthdate(updateEmployeeDTO.birthdate());
        updatedEmployee.setPix(updateEmployeeDTO.pix().trim());
        updatedEmployee.setContact(MapperUtils.normalizeContact(updateEmployeeDTO.contact()));
        updatedEmployee.setCpf(MapperUtils.normalizeCpf(updateEmployeeDTO.cpf()));
        updatedEmployee.setEmail(updateEmployeeDTO.email().trim());
        updatedEmployee.setUpdatedAt(applicationClock.instant());
        return updatedEmployee;
    }

}
