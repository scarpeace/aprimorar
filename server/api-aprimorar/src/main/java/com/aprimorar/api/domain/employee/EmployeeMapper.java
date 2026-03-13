package com.aprimorar.api.domain.employee;

import java.time.Clock;

import com.aprimorar.api.enums.Duty;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.enums.Role;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class EmployeeMapper {

    private final Clock applicationClock;

    public EmployeeMapper(Clock applicationClock){
        this.applicationClock = applicationClock;
    }

    public Employee convertToEntity(EmployeeRequestDTO request) {
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

    public EmployeeResponseDTO convertToDto(Employee entity) {

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPix(),
                MapperUtils.formatContact(entity.getContact()),
                MapperUtils.formatCpf(entity.getCpf()),
                entity.getEmail(),
                entity.getDuty(),
                entity.getArchivedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}
