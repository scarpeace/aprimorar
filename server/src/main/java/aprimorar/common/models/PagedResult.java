package aprimorar.common.models;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        /** Page number exposto pela API, começando em 1. */
        int pageNumber,
        int pageSize,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {

    public PagedResult(Page<T> page) {
        this(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    public <R> PagedResult<R> map(Function<? super T, ? extends R> mapper) {
        return new PagedResult<>(
                data.stream().<R>map(mapper).toList(),
                totalElements,
                pageNumber,
                pageSize,
                totalPages,
                isFirst,
                isLast,
                hasNext,
                hasPrevious
        );
    }
}
