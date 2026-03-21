package com.aprimorar.api.domain.parent;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parent", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(
        operationId = "createParent",
        summary = "Criar novo responsável",
        description = "Cria um novo responsável com os dados fornecidos."
    )
    @PostMapping
    public ResponseEntity<ParentResponseDTO> createParent(@RequestBody @Valid ParentRequestDTO request) {

        ParentResponseDTO createdParent = parentService.createParent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParent);
    }

    @Operation(
        operationId = "getParents",
        summary = "Obter todos os responsáveis",
        description = "Retorna todos os responsáveis cadastrados."
    )
    @GetMapping("/all")
    public ResponseEntity<List<ParentResponseDTO>> getParents() {

        List<ParentResponseDTO> parents = parentService.getParents();
        return ResponseEntity.ok(parents);
    }

    @Operation(
        operationId = "getPaginatedParents",
        summary = "Obter responsáveis paginados",
        description = "Retorna os responsáveis cadastrados com paginação."
    )
    @GetMapping
    public ResponseEntity<Page<ParentResponseDTO>> getPaginatedParents(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<ParentResponseDTO> parents = parentService.getPaginatedParents(pageable, search);
        return ResponseEntity.ok(parents);
    }

    @Operation(
        operationId = "getParentById",
        summary = "Obter responsável por ID",
        description = "Retorna um único responsável com base no ID fornecido."
    )
    @GetMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> getParentById(@PathVariable UUID parentId) {

        ParentResponseDTO parent = parentService.findById(parentId);
        return ResponseEntity.ok(parent);
    }

    @Operation(
        operationId = "updateParent",
        summary = "Atualizar responsável",
        description = "Atualiza os dados de um responsável existente com base no ID fornecido."
    )
    @PutMapping("/{parentId}")
    public ResponseEntity<ParentResponseDTO> updateParent(
            @PathVariable UUID parentId,
            @RequestBody @Valid ParentRequestDTO request
    ) {

        ParentResponseDTO updatedParent = parentService.updateParent(parentId, request);
        return ResponseEntity.ok(updatedParent);
    }

    @Operation(
        operationId = "archiveParent",
        summary = "Arquivar responsável",
        description = "Arquiva um responsável com base no ID fornecido."
    )
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archiveParent(@PathVariable UUID id) {

        parentService.archiveParent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        operationId = "unarchiveParent",
        summary = "Desarquivar responsável",
        description = "Desarquiva um responsável com base no ID fornecido."
    )
    @PatchMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveParent(@PathVariable UUID id) {

        parentService.unarchiveParent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        operationId = "deleteParent",
        summary = "Deletar responsável",
        description = "Deleta um responsável com base no ID fornecido."
    )
    @DeleteMapping("/{parentId}")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID parentId) {

        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }

}
