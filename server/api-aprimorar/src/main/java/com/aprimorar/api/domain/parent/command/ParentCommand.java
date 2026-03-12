package com.aprimorar.api.domain.parent.command;

public record ParentCommand(
        String name,
        String email,
        String contact,
        String cpf
) {
}
