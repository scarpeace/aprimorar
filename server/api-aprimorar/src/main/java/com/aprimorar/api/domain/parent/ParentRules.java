package com.aprimorar.api.domain.parent;

import com.aprimorar.api.domain.parent.exception.InvalidParentException;

public class ParentRules {

    public static void validate(Parent parent){
        validateRequiredFields(parent);
    }

    private static void validateRequiredFields(Parent parent) {
        if (parent.getId() == null) {
            throw new InvalidParentException("ID do responsável é obrigatória");
        }

        if (parent.getName() == null || parent.getName().isBlank()) {
            throw new InvalidParentException("Nome do responsável é obrigatório");
        }
        if (parent.getEmail() == null || parent.getEmail().isBlank()){
            throw new InvalidParentException("Email do responsável é obrigatório");
        }
        if (parent.getContact() == null || parent.getContact().isBlank()) {
            throw new InvalidParentException("Contato do responsável é obrigatório");
        }
        if (parent.getCpf() == null || parent.getCpf().isBlank()) {
            throw new InvalidParentException("CPF do responsável é obrigatório");
        }
    }
}
