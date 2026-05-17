package aprimorar.registration.employee.api;

import aprimorar.registration.employee.api.dto.EmployeeOptionsDTO;
import aprimorar.registration.employee.api.dto.EmployeeRequestDTO;
import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto);

    PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived);

    List<EmployeeOptionsDTO> getEmployeeOptions();

    EmployeeResponseDTO findById(UUID employeeId);

    EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request);

    boolean existsById(UUID employeeId);

    void deleteEmployee(UUID employeeId);

    void archiveEmployee(UUID employeeId);

    void unarchiveEmployee(UUID employeeId);
}
