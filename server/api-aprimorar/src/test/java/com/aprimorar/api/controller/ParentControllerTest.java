package com.aprimorar.api.controller;

import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.exception.handler.GlobalExceptionHandler;
import com.aprimorar.api.service.ParentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParentControllerTest {

    @Mock
    private ParentService parentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        ParentController controller = new ParentController(parentService);
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-08T12:00:00Z"), ZoneOffset.UTC);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler(fixedClock))
                .build();
    }

    @Test
    @DisplayName("lists active parents with default pagination")
    void listActiveParents() throws Exception {
        when(parentService.listActiveParents(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new ParentSummaryDTO(UUID.randomUUID(), "Maria")), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/v1/parents/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Maria"));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(parentService).listActiveParents(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        Sort.Order sortOrder = pageable.getSort().getOrderFor("name");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertNotNull(sortOrder);
    }

    @Test
    @DisplayName("returns 400 for invalid pagination input")
    void invalidPagination() throws Exception {
        mockMvc.perform(get("/v1/parents/active").param("size", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(parentService);
    }

    @Test
    @DisplayName("returns 400 for invalid sort field")
    void invalidSortBy() throws Exception {
        mockMvc.perform(get("/v1/parents/active").param("sortBy", "email"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("REQUEST_ERROR"));

        verifyNoInteractions(parentService);
    }
}
