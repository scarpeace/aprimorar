package com.aprimorar.api.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageQuery {

    @Min(value = 0, message = "Parâmetro 'page' deve ser >= 0")
    private int page = 0;

    @Min(value = 1, message = "Parâmetro 'size' deve estar entre 1 e 100")
    @Max(value = 100, message = "Parâmetro 'size' deve estar entre 1 e 100")
    private int size = 20;

    private String sortBy;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
