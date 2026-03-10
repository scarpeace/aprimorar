package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.entity.Employee;
import com.aprimorar.api.util.MapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

//TODO Apply sanitization on way in

@Component
public class EmployeeMapper {

    private final MapperUtils mapperUtils;

    public EmployeeMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public static Employee toEntity(EmployeeRequestDTO employeeRequestDTO) {
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


    public EmployeeResponseDTO toDto(Employee entity) {

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPix(),
                mapperUtils.formatContact(entity.getContact()),
                mapperUtils.formatCpf(entity.getCpf()),
                entity.getEmail(),
                entity.getRole(),
                entity.getArchivedAt() == null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateFromDto(UpdateEmployeeDTO dto, Employee entity) {
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
            entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        }
        if (dto.role() != null) {
            entity.setRole(dto.role());
        }
    }

    public String jsonAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
