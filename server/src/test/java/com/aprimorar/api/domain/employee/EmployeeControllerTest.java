package com.aprimorar.api.domain.employee;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aprimorar.api.domain.employee.dto.EmployeeSummaryDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    private static final UUID EMPLOYEE_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private Clock clock;

    @Test
    @DisplayName("should return summary when request is valid")
    void shouldReturnSummaryWhenRequestIsValid() throws Exception {
        EmployeeSummaryDTO summary = new EmployeeSummaryDTO(10L, new BigDecimal("1500.00"), new BigDecimal("140.00"));

        when(employeeService.getSummary(EMPLOYEE_ID, null, null)).thenReturn(summary);

        mockMvc.perform(get("/v1/employees/{id}/summary", EMPLOYEE_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalEvents").value(10))
            .andExpect(jsonPath("$.totalPaid").value(1500.00))
            .andExpect(jsonPath("$.totalUnpaid").value(140.00));
    }
}
