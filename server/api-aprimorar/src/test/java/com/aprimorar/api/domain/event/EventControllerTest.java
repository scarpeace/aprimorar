package com.aprimorar.api.domain.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.enums.EventContent;
import com.aprimorar.api.enums.EventStatus;
import com.aprimorar.api.enums.FinancialStatus;
import com.aprimorar.api.shared.PageDTO;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    // @Test
    // @DisplayName("should list events with filters")
    // void shouldListEventsWithFilters() throws Exception {
    //     UUID eventId = UUID.randomUUID();
    //     EventResponseDTO response = new EventResponseDTO(
    //         eventId,
    //         "Description",
    //         EventContent.AULA,
    //         Instant.parse("2023-11-20T14:00:00Z"),
    //         Instant.parse("2023-11-20T16:00:00Z"),
    //         BigDecimal.valueOf(150),
    //         BigDecimal.valueOf(100),
    //         UUID.randomUUID(),
    //         "Student Name",
    //         UUID.randomUUID(),
    //         "Employee Name",
    //         EventStatus.SCHEDULED,
    //         FinancialStatus.PENDING,
    //         FinancialStatus.PENDING,
    //         Instant.now(),
    //         null
    //     );

    //     PageDTO<EventResponseDTO> page = new PageDTO<>(new PageImpl<>(List.of(response)));

    //     when(eventService.getEvents(any(Pageable.class), eq("search"), any(Instant.class), any(Instant.class), eq(EventStatus.SCHEDULED)))
    //         .thenReturn(page);

    //     mockMvc.perform(get("/v1/events")
    //             .param("search", "search")
    //             .param("startDate", "2023-11-20T00:00:00Z")
    //             .param("endDate", "2023-11-20T23:59:59Z")
    //             .param("status", "SCHEDULED")
    //             .contentType(MediaType.APPLICATION_JSON))
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$.content[0].eventId").value(eventId.toString()))
    //         .andExpect(jsonPath("$.content[0].status").value("SCHEDULED"));
    // }
}
