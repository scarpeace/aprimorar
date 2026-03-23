package com.aprimorar.api.domain.parent.web;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.ParentService;
import com.aprimorar.api.domain.parent.dto.ParentOptionDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/parents")
public class ParentController implements ParentControllerDocs {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ParentResponseDTO> createParent(@RequestBody @Valid ParentRequestDTO request) {
        ParentResponseDTO createdParent = parentService.createParent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParent);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ParentResponseDTO>> getParents(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(parentService.getParents(pageable, search));
    }

    @Override
    @GetMapping("/options")
    public ResponseEntity<List<ParentOptionDTO>> getParentsOptions() {
        return ResponseEntity.ok(parentService.getParentOptions());
    }

    @Override
    @GetMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> getParentById(@PathVariable UUID parentId) {
        ParentResponseDTO parent = parentService.findById(parentId);
        return ResponseEntity.ok(parent);
    }

    @Override
    @PutMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> updateParent(
            @PathVariable UUID parentId,
            @RequestBody @Valid ParentRequestDTO request
    ) {
        ParentResponseDTO updatedParent = parentService.updateParent(parentId, request);
        return ResponseEntity.ok(updatedParent);
    }

    @Override
    @DeleteMapping("/{parentId}")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveParent(@PathVariable UUID id) {
        parentService.archiveParent(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveParent(@PathVariable UUID id) {
        parentService.unarchiveParent(id);
        return ResponseEntity.noContent().build();
    }

}
