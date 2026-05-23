package aprimorar.registration.employee.internal;

import aprimorar.registration.employee.api.contract.dto.EmployeeCountSummaryDTO;
import aprimorar.registration.employee.api.contract.dto.EmployeeOptionsDTO;
import aprimorar.registration.employee.api.contract.dto.EmployeeRequestDTO;
import aprimorar.registration.employee.api.contract.dto.EmployeeResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived);

    EmployeeResponseDTO findById(UUID employeeId);

    EmployeeCountSummaryDTO getSummary();

    List<EmployeeOptionsDTO> getEmployeeOptions();

    EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO dto);

    void deleteEmployee(UUID employeeId);

    void archiveEmployee(UUID employeeId);

    void unarchiveEmployee(UUID employeeId);
}
