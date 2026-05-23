package aprimorar.registration.employee.internal;

import org.springframework.stereotype.Component;
import aprimorar.registration.employee.api.dto.EmployeeRequestDTO;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.registration.shared.address.AddressMapper;


@Component
public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDTO dto) {
        return new Employee(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.cpf(),
            dto.email(),
            dto.duty(),
            AddressMapper.toEntity(dto.address())
        );
    }

    public static EmployeeResponseDTO toDto(Employee employee) {
        return new EmployeeResponseDTO(
            employee.getId(),
            employee.getName(),
            employee.getBirthdate(),
            employee.getPix(),
            employee.getContact(),
            employee.getCpf(),
            employee.getEmail(),
            employee.getDuty(),
            AddressMapper.toDto(employee.getAddress()),
            employee.getActive(),
            employee.getCreatedAt(),
            employee.getUpdatedAt()
        );
    }
}
