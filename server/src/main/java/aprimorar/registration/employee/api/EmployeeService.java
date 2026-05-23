package aprimorar.registration.employee.api;

import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import aprimorar.shared.PageDTO;

public interface EmployeeService {

    //TODO: Talvez dê pra tirar esse método daqui, não tenho certeza.
    PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived);
    EmployeeResponseDTO findById(UUID employeeId);
}
