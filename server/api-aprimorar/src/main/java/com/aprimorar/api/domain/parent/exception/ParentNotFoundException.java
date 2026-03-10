package com.aprimorar.api.domain.parent.exception;

import java.util.UUID;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(UUID id) {
        super("Responsável com o ID informado não foi encontrado no Bando de Dados:  " + id);
    }
}
