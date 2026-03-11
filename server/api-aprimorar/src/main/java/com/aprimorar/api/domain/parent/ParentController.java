package com.aprimorar.api.domain.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all parents", description = "Retrieves parents from database with pagination and optional archived records")
    @GetMapping
    public ResponseEntity<Page<ParentResponseDTO>> listParents(@PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParentResponseDTO> parents = parentService.getParents(pageable);
        return ResponseEntity.ok(parents);
    }

}
