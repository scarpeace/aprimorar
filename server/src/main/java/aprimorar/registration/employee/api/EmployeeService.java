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

//    List<UUID> findIdsByNameContaining(String name);

//    java.util.Optional<UUID> findIdByEmail(String email);

    EmployeeResponseDTO findById(UUID employeeId);


//    Map<UUID, EmployeeResponseDTO> findByIds(Collection<UUID> employeeIds);

//    boolean existsById(UUID id);e

//    EmployeeResponseDTO getReferenceById(UUID id);

//    EmployeeSummaryDTO getSummary(UUID employeeId, Instant startDate, Instant endDate);

    EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request);

    boolean existsById(UUID employeeId);

    void deleteEmployee(UUID employeeId);

    void archiveEmployee(UUID employeeId);

    void unarchiveEmployee(UUID employeeId);
}
