package aprimorar.registration.employee.api.contract;

import aprimorar.registration.employee.api.contract.dto.EmployeeResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeQueryApi {

    PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived);

    EmployeeResponseDTO findById(UUID employeeId);
}
