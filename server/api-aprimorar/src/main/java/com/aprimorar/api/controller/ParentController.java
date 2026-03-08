package com.aprimorar.api.controller;

import com.aprimorar.api.dto.common.PageQuery;
import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.service.ParentService;
import com.aprimorar.api.util.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "createdAt", "updatedAt");

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all active parents", description = "Retrieves all ACTIVE parents from database with pagination (id + name only)")
    @GetMapping("/active")
    public ResponseEntity<Page<ParentSummaryDTO>> listActiveParents(
            @Valid @ModelAttribute PageQuery pageQuery
    )
    {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "name", ALLOWED_SORT_FIELDS);
        Page<ParentSummaryDTO> activeParents = parentService.listActiveParents(pageable);
        return ResponseEntity.ok(activeParents);
    }
}
