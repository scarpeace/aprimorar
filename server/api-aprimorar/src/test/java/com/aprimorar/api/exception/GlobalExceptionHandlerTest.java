package com.aprimorar.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
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
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Aluno não encontrado no banco de dados"))
                .andExpect(jsonPath("$.instance").value("/test-errors/not-found"))
                .andExpect(jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.name()))
                .andExpect(jsonPath("$.timestamp").value(FIXED_INSTANT.toString()));
    }

    @Test
    @DisplayName("should return ProblemDetail for conflict exceptions")
    void shouldReturnProblemDetailForConflictExceptions() throws Exception {
        mockMvc.perform(post("/test-errors/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Conflito de negócio"))
                .andExpect(jsonPath("$.detail").value("Responsável já existe no banco de dados"))
                .andExpect(jsonPath("$.instance").value("/test-errors/conflict"))
                .andExpect(jsonPath("$.code").value(ErrorCode.BUSINESS_ERROR.name()))
                .andExpect(jsonPath("$.timestamp").value(FIXED_INSTANT.toString()));
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
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Falha de validação"))
                .andExpect(jsonPath("$.detail").value("Nome do estudante é obrigatório"))
                .andExpect(jsonPath("$.instance").value("/test-errors/validation"))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.timestamp").value(FIXED_INSTANT.toString()));
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
        void validation(@Valid @RequestBody StudentRequestDTO request) {
            // Validation is handled before this method is reached.
        }
    }
}
