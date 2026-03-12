package com.aprimorar.api.domain.student.command;

import java.time.LocalDate;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;

public record StudentCommand(
        String name,
        LocalDate birthdate,
        String cpf,
        String school,
        String contact,
        String email,
        Address address,
        Parent parent
) {
}
