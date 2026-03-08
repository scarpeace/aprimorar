package com.aprimorar.api.controller;

import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.enums.Role;
import com.aprimorar.api.exception.domain.EmployeeNotFoundException;
import com.aprimorar.api.exception.handler.GlobalExceptionHandler;
import com.aprimorar.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        EmployeeController controller = new EmployeeController(employeeService);
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-08T12:00:00Z"), ZoneOffset.UTC);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler(fixedClock))
                .build();
    }

    @Nested
    @DisplayName("listEmployees")
    class ListEmployees {

        @Test
        @DisplayName("uses default pagination for list endpoint")
        void defaultPagination() throws Exception {
            when(employeeService.listEmployees(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

            mockMvc.perform(get("/v1/employees"))
                    .andExpect(status().isOk());

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(employeeService).listEmployees(pageableCaptor.capture());

            Pageable pageable = pageableCaptor.getValue();
            Sort.Order sortOrder = pageable.getSort().getOrderFor("name");
            assertEquals(0, pageable.getPageNumber());
            assertEquals(20, pageable.getPageSize());
            assertNotNull(sortOrder);
        }

        @Test
        @DisplayName("returns 400 for invalid sort field")
        void invalidSortBy() throws Exception {
            mockMvc.perform(get("/v1/employees").param("sortBy", "cpf"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("REQUEST_ERROR"));

            verifyNoInteractions(employeeService);
        }

        @Test
        @DisplayName("lists active employees with provided pagination")
        void listActiveEmployees() throws Exception {
            when(employeeService.listActiveEmployees(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(employeeResponse()), PageRequest.of(1, 10), 1));

            mockMvc.perform(get("/v1/employees/active")
                            .param("page", "1")
                            .param("size", "10")
                            .param("sortBy", "updatedAt"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Ana"));

            verify(employeeService).listActiveEmployees(any(Pageable.class));
        }
    }

    @Test
    @DisplayName("returns employee details by id")
    void getById() throws Exception {
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(eq(employeeId))).thenReturn(employeeResponse());

        mockMvc.perform(get("/v1/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(employeeService).findById(employeeId);
    }

    @Test
    @DisplayName("returns 404 when employee is not found")
    void getByIdNotFound() throws Exception {
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(eq(employeeId))).thenThrow(new EmployeeNotFoundException(employeeId));

        mockMvc.perform(get("/v1/employees/{employeeId}", employeeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EMPLOYEE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Funcionário não encontrado"));
    }

    @Test
    @DisplayName("delete endpoint delegates to service")
    void deleteEmployee() throws Exception {
        UUID employeeId = UUID.randomUUID();

        mockMvc.perform(delete("/v1/employees/{employeeId}", employeeId))
                .andExpect(status().isNoContent());

        verify(employeeService).softDeleteEmployee(employeeId);
    }

    private EmployeeResponseDTO employeeResponse() {
        return new EmployeeResponseDTO(
                UUID.randomUUID(),
                "Ana",
                LocalDate.of(1990, 5, 10),
                "pix-key",
                "(61)99999-9999",
                "123.456.789-00",
                "ana@email.com",
                Role.ADMIN,
                true,
                Instant.parse("2026-03-08T10:00:00Z"),
                Instant.parse("2026-03-08T11:00:00Z")
        );
    }
}
