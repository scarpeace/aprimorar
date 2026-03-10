package com.aprimorar.api.domain.employee;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.entity.Employee;
import com.aprimorar.api.shared.MapperUtils;

//TODO Apply sanitization on way in

@Component
public class EmployeeMapper {

    private final MapperUtils mapperUtils;

    public EmployeeMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public static Employee convertToEntity(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee();

        employee.setName(employeeRequestDTO.name());
        employee.setBirthdate(employeeRequestDTO.birthdate());
        employee.setPix(employeeRequestDTO.pix());
        employee.setContact(employeeRequestDTO.contact());
        employee.setCpf(employeeRequestDTO.contact());
        employee.setEmail(employeeRequestDTO.email());
        employee.setRole(employeeRequestDTO.role());
        return employee;
    }


    public static EmployeeResponseDTO convertToDto(Employee entity) {

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

    public static void updateFromDto(UpdateEmployeeDTO dto, Employee entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.birthdate() != null) {
            entity.setBirthdate(dto.birthdate());
        }
        if (dto.pix() != null) {
            entity.setPix(dto.pix());
        }
        if (dto.contact() != null) {
            entity.setContact(MapperUtils.sanitizeContact(dto.contact()));
        }
        if (dto.cpf() != null) {
            entity.setCpf(MapperUtils.sanitizeCpf(dto.cpf()));
        }
        if (dto.email() != null) {
            entity.setEmail(MapperUtils.sanitizeEmail(dto.email()));
        }
        if (dto.role() != null) {
            entity.setRole(dto.role());
        }
    }
}
