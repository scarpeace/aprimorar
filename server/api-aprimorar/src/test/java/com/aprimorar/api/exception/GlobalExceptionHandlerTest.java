package com.aprimorar.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.student.dto.StudentCreateDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

class GlobalExceptionHandlerTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2026-03-21T10:00:00Z");

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new TestErrorController())
                .setControllerAdvice(new GlobalExceptionHandler(Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC)))
                .setValidator(validator)
                .setMessageConverters(
                        new MappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder.json().build())
                )
                .build();
    }

    @Test
    @DisplayName("should return ProblemDetail for not found exceptions")
    void shouldReturnProblemDetailForNotFoundExceptions() throws Exception {
        mockMvc.perform(get("/test-errors/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Aluno não encontrado no banco de dados"))
                .andExpect(jsonPath("$.uri").value("/test-errors/not-found"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.RESOURCE_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("should return ProblemDetail for conflict exceptions")
    void shouldReturnProblemDetailForConflictExceptions() throws Exception {
        mockMvc.perform(post("/test-errors/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Responsável já existe no banco de dados"))
                .andExpect(jsonPath("$.uri").value("/test-errors/conflict"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.CONFLICT.name()));
    }

    @Test
    @DisplayName("should return ProblemDetail for validation failures")
    void shouldReturnProblemDetailForValidationFailures() throws Exception {
        mockMvc.perform(post("/test-errors/validation")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "",
                                  "birthdate": "2012-05-10",
                                  "cpf": "12345678901",
                                  "school": "Escola Teste",
                                  "contact": "11988887766",
                                  "email": "aluno@teste.com",
                                  "address": {
                                    "street": "Rua das Flores",
                                    "number": "123",
                                    "complement": "Casa",
                                    "district": "Centro",
                                    "city": "Sao Paulo",
                                    "state": "SP",
                                    "zip": "04711-230"
                                  },
                                  "parentId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Nome do aluno é obrigatório"))
                .andExpect(jsonPath("$.uri").value("/test-errors/validation"))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_ERROR.name()));
    }

    @RestController
    @RequestMapping("/test-errors")
    static class TestErrorController {

        @GetMapping("/not-found")
        void notFound() {
            throw new StudentNotFoundException("Aluno não encontrado no banco de dados");
        }

        @PostMapping("/conflict")
        void conflict() {
            throw new ParentAlreadyExistsException("Responsável já existe no banco de dados");
        }

        @PostMapping("/validation")
        void validation(@Valid @RequestBody StudentCreateDTO request) {
            // Validation is handled before this method is reached.
        }
    }
}
