package aprimorar.registration.employee.internal;

import org.springframework.stereotype.Component;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;


@Component
public class EmployeeMapper {

    public EmployeeResponseDTO toResponseDto(Employee employee) {
        return new EmployeeResponseDTO(
            employee.getId(),
            employee.getName(),
            employee.getBirthdate(),
            employee.getPix(),
            employee.getContact(),
            employee.getCpf(),
            employee.getEmail(),
            employee.getDuty(),
            employee.getAddress().toResponseDto(),
            employee.getActive(),
            employee.getCreatedAt(),
            employee.getUpdatedAt()
        );
    }
}
