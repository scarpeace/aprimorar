package com.aprimorar.api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

public final class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable buildPageable(
            int page,
            int size,
            String sortBy,
            String defaultSortBy,
            Set<String> allowedSortFields
    ) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametro 'page' deve ser >= 0");
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametro 'size' deve estar entre 1 e 100");
        }

        String normalizedSortBy = sortBy == null ? defaultSortBy : sortBy.trim();
        if (normalizedSortBy.isEmpty()) {
            normalizedSortBy = defaultSortBy;
        }

        if (!allowedSortFields.contains(normalizedSortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametro 'sortBy' invalido");
        }

        return PageRequest.of(page, size, Sort.by(normalizedSortBy));
    }
}
