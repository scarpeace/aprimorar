package com.aprimorar.api.controller;

import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.service.ParentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all active parents", description = "Retrieves all ACTIVE parents from database with pagination (id + name only)")
    @Transactional(readOnly = true)
    @GetMapping("/active")
    public ResponseEntity<Page<ParentSummaryDTO>> listActiveParents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ParentSummaryDTO> activeParents = parentService.listActiveParents(pageable);
        return ResponseEntity.ok(activeParents);
    }
}
