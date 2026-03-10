package com.aprimorar.api.domain.parent;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.dto.ParentSummaryDTO;
import com.aprimorar.api.requests.common.PageQuery;
import com.aprimorar.api.shared.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "createdAt", "updatedAt");

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all parents", description = "Retrieves parents from database with pagination and optional archived records")
    @GetMapping
    public ResponseEntity<Page<ParentSummaryDTO>> listParents(
            @Valid @ModelAttribute PageQuery pageQuery,
            @RequestParam(defaultValue = "false") boolean includeArchived
    )
    {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "name", ALLOWED_SORT_FIELDS);
        Page<ParentSummaryDTO> parents = parentService.listParents(pageable, includeArchived);
        return ResponseEntity.ok(parents);
    }

}
