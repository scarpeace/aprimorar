package com.aprimorar.api.domain.employee;

import com.aprimorar.api.enums.Role;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.shared.MapperUtils;

import java.time.Clock;
import java.time.Instant;


@Component
public class EmployeeMapper {

    private final Clock applicationClock;

    public EmployeeMapper(Clock applicationClock){
        this.applicationClock = applicationClock;
    }

    public Employee convertToEntity(EmployeeRequestDTO updateEmployeeDTO) {
        Employee employee = new Employee();

        employee.setName(updateEmployeeDTO.name());
        employee.setBirthdate(updateEmployeeDTO.birthdate());
        employee.setPix(updateEmployeeDTO.pix().trim());
        employee.setContact(MapperUtils.sanitizeContact(updateEmployeeDTO.contact()));
        employee.setCpf(MapperUtils.sanitizeCpf(updateEmployeeDTO.cpf()));
        employee.setEmail(updateEmployeeDTO.email().trim());
        employee.setRole(Role.EMPLOYEE);
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
        updatedEmployee.setPix(updateEmployeeDTO.pix());
        updatedEmployee.setContact(updateEmployeeDTO.contact());
        updatedEmployee.setCpf(updateEmployeeDTO.cpf());
        updatedEmployee.setEmail(updateEmployeeDTO.email());
        updatedEmployee.setUpdatedAt(applicationClock.instant());
        return updatedEmployee;
    }

}
