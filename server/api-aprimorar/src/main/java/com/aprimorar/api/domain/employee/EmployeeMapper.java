package com.aprimorar.api.domain.employee;


import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeCreateDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeUpdateDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class EmployeeMapper {

    public Employee convertToEntityForCreate(EmployeeCreateDTO request) {
        Employee employee = new Employee();

        employee.setName(request.name());
        employee.setBirthdate(request.birthdate());
        employee.setPix(request.pix());
        employee.setContact(MapperUtils.normalizeContact(request.contact()));
        employee.setCpf(MapperUtils.normalizeCpf(request.cpf()));
        employee.setEmail(request.email());
        employee.setDuty(request.duty());
        return employee;
    }

public Employee convertToEntityForUpdate(EmployeeUpdateDTO request) {
        Employee employee = new Employee();

        employee.setName(request.name());
        employee.setBirthdate(request.birthdate());
        employee.setPix(request.pix());
        employee.setContact(MapperUtils.normalizeContact(request.contact()));
        employee.setEmail(request.email());
        employee.setDuty(request.duty());
        return employee;
    }

    public EmployeeResponseDTO convertToDto(Employee entity) {

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPix(),
                entity.getContact(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getDuty(),
                entity.getArchivedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
