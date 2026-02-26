package com.aprimorar.api.exception.domain;

import java.util.UUID;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(UUID id) {
        super("Parent not found with id: " + id);
    }
}
