package com.aprimorar.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record EmployeeResponseDTO(
        String name,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf
) {
}
