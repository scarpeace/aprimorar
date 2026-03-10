package com.aprimorar.api.shared;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        String normalizedSortBy = sortBy == null ? defaultSortBy : sortBy.trim();
        if (normalizedSortBy.isEmpty()) {
            normalizedSortBy = defaultSortBy;
        }

        if (!allowedSortFields.contains(normalizedSortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametro 'sortBy' invalido");
        }

        return PageRequest.of(page, size, Sort.by(normalizedSortBy));
    }

    public static Pageable buildPageable(PageQuery pageQuery, String defaultSortBy, Set<String> allowedSortFields) {
        return buildPageable(
                pageQuery.getPage(),
                pageQuery.getSize(),
                pageQuery.getSortBy(),
                defaultSortBy,
                allowedSortFields
        );
    }
}
