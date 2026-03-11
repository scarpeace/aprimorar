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

    public EmployeeEntity convertToEntity(EmployeeRequestDTO employeeRequestDTO) {
        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setName(employeeRequestDTO.name());
        employeeEntity.setBirthdate(employeeRequestDTO.birthdate());
        employeeEntity.setPix(employeeRequestDTO.pix().trim());
        employeeEntity.setContact(MapperUtils.sanitizeContact(employeeRequestDTO.contact()));
        employeeEntity.setCpf(MapperUtils.sanitizeCpf(employeeRequestDTO.cpf()));
        employeeEntity.setEmail(employeeRequestDTO.email().trim());
        employeeEntity.setRole(Role.EMPLOYEE);
        //TODO Mover essa criação da data para a camada de serviço em todos os mappers
        employeeEntity.setCreatedAt(applicationClock.instant());
        return employeeEntity;
    }

    public EmployeeResponseDTO convertToDto(EmployeeEntity entity) {

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

    public EmployeeEntity updateToEntity(UpdateEmployeeDTO updateEmployeeDTO) {
        EmployeeEntity updatedEmployeeEntity = new EmployeeEntity();

        updatedEmployeeEntity.setName(updateEmployeeDTO.name());
        updatedEmployeeEntity.setBirthdate(updateEmployeeDTO.birthdate());
        updatedEmployeeEntity.setPix(updateEmployeeDTO.pix().trim());
        updatedEmployeeEntity.setContact(MapperUtils.sanitizeContact(updateEmployeeDTO.contact()));
        updatedEmployeeEntity.setCpf(MapperUtils.sanitizeCpf(updateEmployeeDTO.cpf()));
        updatedEmployeeEntity.setEmail(updateEmployeeDTO.email().trim());
        updatedEmployeeEntity.setUpdatedAt(applicationClock.instant());
        return updatedEmployeeEntity;
    }

}
