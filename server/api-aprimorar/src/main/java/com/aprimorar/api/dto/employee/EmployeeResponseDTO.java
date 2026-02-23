package com.aprimorar.api.dto.employee;

import java.time.LocalDate;

public record EmployeeResponseDTO(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf
) {
}
