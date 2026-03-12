package com.aprimorar.api.domain.parent;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all parents", description = "Retrieves parents from database with pagination")
    @GetMapping
    public ResponseEntity<Page<ParentResponseDTO>> listParents(@PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ParentResponseDTO> parents = parentService.getParents(pageable);
        return ResponseEntity.ok(parents);
    }

    @Operation(summary = "Get parent by ID", description = "Retrieves a single parent based on ID")
    @GetMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> getParentById(@PathVariable UUID parentId) {
        ParentResponseDTO parent = parentService.findById(parentId);
        return ResponseEntity.ok(parent);
    }

    @Operation(summary = "Create parent", description = "Creates a new parent")
    @PostMapping
    public ResponseEntity<ParentResponseDTO> createParent(@RequestBody @Valid ParentRequestDTO parentRequestDTO) {
        ParentResponseDTO createdParent = parentService.createParent(parentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParent);
    }

    @Operation(summary = "Update parent", description = "Fully updates parent data")
    @PutMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> updateParent(
            @PathVariable UUID parentId,
            @RequestBody @Valid ParentRequestDTO parentRequestDTO
    ) {
        ParentResponseDTO updatedParent = parentService.updateParent(parentId, parentRequestDTO);
        return ResponseEntity.ok(updatedParent);
    }

    @Operation(summary = "Delete parent", description = "Deletes a parent based on ID")
    @DeleteMapping("/{parentId}")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }

}
