package com.aprimorar.api.exception.handler;

import com.aprimorar.api.exception.domain.StudentNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-03-08T12:00:00Z"), ZoneOffset.UTC);
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler(fixedClock))
                .build();
    }

    @Test
    @DisplayName("returns structured 404 for domain not found exceptions")
    void studentNotFound() throws Exception {
        mockMvc.perform(get("/test/students/{studentId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("STUDENT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Estudante não encontrado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("returns structured 400 for validation errors")
    void validationError() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("name: Nome é obrigatório"));
    }

    @Test
    @DisplayName("returns structured 400 for ResponseStatusException")
    void responseStatusException() throws Exception {
        mockMvc.perform(get("/test/request-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("REQUEST_ERROR"))
                .andExpect(jsonPath("$.message").value("Parâmetro inválido"))
                .andExpect(jsonPath("$.path").value("/test/request-error"));
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/students/{studentId}")
        String studentNotFound(@PathVariable UUID studentId) {
            throw new StudentNotFoundException(studentId);
        }

        @PostMapping("/validation")
        String validation(@Valid @RequestBody TestRequest request) {
            return request.name();
        }

        @GetMapping("/request-error")
        String requestError() {
            throw new ResponseStatusException(BAD_REQUEST, "Parâmetro inválido");
        }
    }

    record TestRequest(
            @NotBlank(message = "Nome é obrigatório")
            String name
    ) {
    }
}
