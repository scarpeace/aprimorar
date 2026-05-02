package com.aprimorar.api.domain.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aprimorar.api.shared.PageDTO;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private java.time.Clock clock;

    @Test
    void getEventsByEmployeeId_ShouldCallServiceWithCorrectParams() throws Exception {
        UUID employeeId = UUID.randomUUID();
        String studentName = "John";

        when(eventService.getEventsByEmployeeId(any(Pageable.class), eq(employeeId), eq(studentName), any(), any(), any()))
            .thenReturn(new PageDTO<>(new PageImpl<>(Collections.emptyList())));

        mockMvc.perform(get("/v1/events/{id}/employee", employeeId)
                .param("studentName", studentName))
            .andExpect(status().isOk());

        verify(eventService).getEventsByEmployeeId(any(Pageable.class), eq(employeeId), eq(studentName), any(), any(), any());
    }

    @Test
    void getEventsByEmployeeId_WithSort_ShouldHandlePageable() throws Exception {
        UUID employeeId = UUID.randomUUID();

        when(eventService.getEventsByEmployeeId(any(Pageable.class), eq(employeeId), any(), any(), any(), any()))
            .thenReturn(new PageDTO<>(new PageImpl<>(Collections.emptyList())));

        mockMvc.perform(get("/v1/events/{id}/employee", employeeId)
                .param("sort", "startDate,desc"))
            .andExpect(status().isOk());

        verify(eventService).getEventsByEmployeeId(any(Pageable.class), eq(employeeId), any(), any(), any(), any());
    }
}
