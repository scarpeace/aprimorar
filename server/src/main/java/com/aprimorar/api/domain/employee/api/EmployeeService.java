package com.aprimorar.api.domain.employee.api;

import com.aprimorar.api.domain.employee.api.dto.EmployeeOptionsDTO;
import com.aprimorar.api.domain.employee.api.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.api.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.api.dto.EmployeeSummaryDTO;
import com.aprimorar.api.shared.PageDTO;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto);

    PageDTO<EmployeeResponseDTO> getEmployees(Pageable pageable, String search, Boolean archived);

    List<EmployeeOptionsDTO> getEmployeeOptions();

    List<UUID> findIdsByNameContaining(String name);

    java.util.Optional<UUID> findIdByEmail(String email);

    EmployeeResponseDTO findById(UUID employeeId);

    Map<UUID, EmployeeResponseDTO> findByIds(Collection<UUID> employeeIds);

    boolean existsById(UUID id);

    EmployeeResponseDTO getReferenceById(UUID id);

    EmployeeSummaryDTO getSummary(UUID employeeId, Instant startDate, Instant endDate);

    EmployeeResponseDTO updateEmployee(UUID employeeId, EmployeeRequestDTO request);

    void deleteEmployee(UUID employeeId);

    void archiveEmployee(UUID employeeId);

    void unarchiveEmployee(UUID employeeId);
}
