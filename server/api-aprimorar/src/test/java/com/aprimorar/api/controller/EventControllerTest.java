package com.aprimorar.api.controller;

import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.dto.event.EventFilter;
import com.aprimorar.api.exception.handler.GlobalExceptionHandler;
import com.aprimorar.api.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-08T12:00:00Z"), ZoneOffset.UTC);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler(fixedClock))
                .build();
    }

    @Nested
    @DisplayName("request validation")
    class RequestValidation {

        @Test
        @DisplayName("returns 400 when creating event without content")
        void createWithoutContent() throws Exception {
            mockMvc.perform(post("/v1/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBodyWithoutContent()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.message", containsString("content")));

            verifyNoInteractions(eventService);
        }

        @Test
        @DisplayName("allows partial update when content is omitted")
        void updateWithoutContent() throws Exception {
            when(eventService.updateEvent(eq(1L), any())).thenReturn(eventResponseWithContent());

            mockMvc.perform(patch("/v1/events/{eventId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBodyWithoutContent()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("ENEM"));

            verify(eventService).updateEvent(eq(1L), any());
        }

        @Test
        @DisplayName("returns 400 when content enum value is invalid")
        void invalidEnumContent() throws Exception {
            mockMvc.perform(post("/v1/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bodyWithInvalidContent()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("MALFORMED_REQUEST"));

            verifyNoInteractions(eventService);
        }
    }

    @Nested
    @DisplayName("listEvents")
    class ListEvents {

            @Test
        @DisplayName("includes content field in list response")
        void includesContentInList() throws Exception {
            when(eventService.listEvents(any(Pageable.class), any(EventFilter.class)))
                    .thenReturn(new PageImpl<>(List.of(eventResponseWithContent()), PageRequest.of(0, 20), 1));

            mockMvc.perform(get("/v1/events"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].content").value("ENEM"));

            verify(eventService).listEvents(any(Pageable.class), any(EventFilter.class));
        }

        @Test
        @DisplayName("returns 400 for invalid sortBy")
        void invalidSortBy() throws Exception {
            mockMvc.perform(get("/v1/events").param("sortBy", "studentId"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(eventService);
        }

        @Test
        @DisplayName("returns 400 for negative page")
        void negativePage() throws Exception {
            mockMvc.perform(get("/v1/events").param("page", "-1"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(eventService);
        }

        @Test
        @DisplayName("returns 400 for size less than 1")
        void invalidSize() throws Exception {
            mockMvc.perform(get("/v1/events").param("size", "0"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(eventService);
        }

        @Test
        @DisplayName("passes filters to service")
        void forwardsFiltersToService() throws Exception {
            when(eventService.listEvents(any(Pageable.class), any(EventFilter.class)))
                    .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

            mockMvc.perform(get("/v1/events")
                            .param("studentId", UUID.randomUUID().toString())
                            .param("employeeId", UUID.randomUUID().toString())
                            .param("start", "2027-06-01T10:00:00")
                            .param("end", "2027-06-01T11:00:00"))
                    .andExpect(status().isOk());

            verify(eventService).listEvents(any(Pageable.class), any(EventFilter.class));
        }
    }

    @Test
    @DisplayName("includes content field in detail response")
    void getByIdIncludesContent() throws Exception {
        when(eventService.findById(eq(1L))).thenReturn(eventResponseWithContent());

        mockMvc.perform(get("/v1/events/{eventId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("ENEM"))
                .andExpect(jsonPath("$.startDateTime").value("01/06/2027 10:00:00"))
                .andExpect(jsonPath("$.endDateTime").value("01/06/2027 11:00:00"))
                .andExpect(jsonPath("$.createdAt").value("08/03/2026 10:00:00"))
                .andExpect(jsonPath("$.updatedAt").value("08/03/2026 11:00:00"));

        verify(eventService).findById(1L);
    }

    private String validBodyWithoutContent() {
        return """
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
    }

    private String bodyWithInvalidContent() {
        return """
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
    }

    private EventResponseDTO eventResponseWithContent() {
        return new EventResponseDTO(
                1L,
                "Physics class",
                "Kinematics review",
                "ENEM",
                java.time.LocalDateTime.of(2027, 6, 1, 10, 0),
                java.time.LocalDateTime.of(2027, 6, 1, 11, 0),
                new BigDecimal("100.00"),
                new BigDecimal("50.00"),
                UUID.randomUUID(),
                "Student Name",
                UUID.randomUUID(),
                "Employee Name",
                Instant.parse("2026-03-08T10:00:00Z"),
                Instant.parse("2026-03-08T11:00:00Z")
        );
    }
}
