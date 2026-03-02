package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.util.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final MapperUtils mapperUtils;

    public EmployeeMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public Employee toEntity(CreateEmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee entity = new Employee();
        entity.setName(dto.name());
        entity.setBirthdate(dto.birthdate());
        entity.setPix(dto.pix());
        entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        entity.setEmail(dto.email());
        entity.setRole(dto.role());
        return entity;
    }

    public EmployeeResponseDTO toDto(Employee entity) {
        if (entity == null) {
            return null;
        }

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPix(),
                mapperUtils.formatContact(entity.getContact()),
                mapperUtils.formatCpf(entity.getCpf()),
                entity.getEmail(),
                entity.getRole(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateFromDto(CreateEmployeeDTO dto, Employee entity) {
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
            entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        }
        if (dto.cpf() != null) {
            entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        }
        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }
        if (dto.role() != null) {
            entity.setRole(dto.role());
        }
    }
}
