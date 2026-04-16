package com.aprimorar.api.domain.employee;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;

@Component
public class EmployeeMapper {

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
