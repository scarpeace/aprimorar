package com.aprimorar.api.controller;

import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.exception.handler.GlobalExceptionHandler;
import com.aprimorar.api.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        EventController controller = new EventController(eventService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should return 400 when creating event without content")
    void createEventWithoutContentShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                  "title": "Physics class",
                  "description": "Kinematics review",
                  "startDateTime": "2027-06-01T10:00:00",
                  "endDateTime": "2027-06-01T11:00:00",
                  "price": 100.00,
                  "payment": 50.00,
                  "studentId": "d320b9db-9e72-42ce-b62f-3dc4fbe6d7eb",
                  "employeeId": "756ecad4-adf8-457f-9188-cbc2c48f3cb8"
                }
                """;

        mockMvc.perform(post("/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("content")));

        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("Should return 400 when updating event without content")
    void updateEventWithoutContentShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                  "title": "Physics class",
                  "description": "Kinematics review",
                  "startDateTime": "2027-06-01T10:00:00",
                  "endDateTime": "2027-06-01T11:00:00",
                  "price": 100.00,
                  "payment": 50.00,
                  "studentId": "d320b9db-9e72-42ce-b62f-3dc4fbe6d7eb",
                  "employeeId": "756ecad4-adf8-457f-9188-cbc2c48f3cb8"
                }
                """;

        mockMvc.perform(patch("/v1/events/{eventId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("content")));

        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("Should return 400 when content enum value is invalid")
    void createEventWithInvalidContentShouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                  "title": "Physics class",
                  "description": "Kinematics review",
                  "startDateTime": "2027-06-01T10:00:00",
                  "endDateTime": "2027-06-01T11:00:00",
                  "price": 100.00,
                  "payment": 50.00,
                  "content": "INVALIDO",
                  "studentId": "d320b9db-9e72-42ce-b62f-3dc4fbe6d7eb",
                  "employeeId": "756ecad4-adf8-457f-9188-cbc2c48f3cb8"
                }
                """;

        mockMvc.perform(post("/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MALFORMED_REQUEST"));

        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("Should include content in events list response")
    void listEventsShouldIncludeContent() throws Exception {
        when(eventService.listEvents(any(Pageable.class), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(new PageImpl<>(List.of(eventResponseWithContent()), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("ENEM"));

        verify(eventService).listEvents(any(Pageable.class), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    @DisplayName("Should include content in event detail response")
    void getEventByIdShouldIncludeContent() throws Exception {
        when(eventService.findById(1L)).thenReturn(eventResponseWithContent());

        mockMvc.perform(get("/v1/events/{eventId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("ENEM"));

        verify(eventService).findById(1L);
    }

    private EventResponseDTO eventResponseWithContent() {
        return new EventResponseDTO(
                1L,
                "Physics class",
                "Kinematics review",
                "ENEM",
                null,
                null,
                new BigDecimal("100.00"),
                new BigDecimal("50.00"),
                UUID.randomUUID(),
                "Student Name",
                UUID.randomUUID(),
                "Employee Name",
                null,
                null
        );
    }
}
