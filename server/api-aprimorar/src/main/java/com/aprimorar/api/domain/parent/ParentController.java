package com.aprimorar.api.domain.parent;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.dto.ParentOptionsDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.shared.PageDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parent", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping()
    @Operation(operationId = "createParent", description = "Cria um novo responsável")
    @ApiResponse(responseCode = "204", description = "Responsável criado com sucesso")
    public ResponseEntity<ParentResponseDTO> createParent(@RequestBody ParentRequestDTO parentRequestDTO) {
        ParentResponseDTO parent = parentService.createParent(parentRequestDTO);
        return ResponseEntity.ok(parent);
    }

    @GetMapping
    @Operation(operationId = "getParents", description = "Retorna uma lista de responsáveis paginada")
    @ApiResponse(responseCode = "200", description = "Lista de responsáveis retornada com sucesso.")
    public ResponseEntity<PageDTO<ParentResponseDTO>> getParents(
            @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived) {
        return ResponseEntity.ok(parentService.getParents(pageable, search));
    }

    @GetMapping("/summary")
    @Operation(operationId = "getParentsOptions", description = "Retorna uma lista de responsáveis para dropdown")
    @ApiResponse(responseCode = "200", description = "Lista de opções retornada com sucesso.")
    public ResponseEntity<List<ParentOptionsDTO>> getParentOptions() {
        return ResponseEntity.ok(parentService.getParentOptions());
    }

    @GetMapping("/{parentId}")
    @Operation(operationId = "getParentById", description = "Retorna um responsável por ID")
    @ApiResponse(responseCode = "200", description = "Responsável retornado com sucesso")
    public ResponseEntity<ParentResponseDTO> getParentById(@PathVariable UUID parentId) {
        ParentResponseDTO parent = parentService.findById(parentId);
        return ResponseEntity.ok(parent);
    }

    @PatchMapping("/{parentId}")
    @Operation(operationId = "updateParent", description = "Atualiza um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável atualizado com sucesso")
    public ResponseEntity<Void> updateParent(@PathVariable UUID parentId, @RequestBody ParentRequestDTO parentRequestDTO) {
        parentService.updateParent(parentId, parentRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{parentId}/archive")
    @Operation(operationId = "archiveParent", description = "Arquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável arquivado com sucesso")
    public ResponseEntity<Void> archiveParent(@PathVariable UUID parentId) {
        parentService.archiveParent(parentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{parentId}/unarchive")
    @Operation(operationId = "unarchiveParent", description = "Desarquiva um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável desarquivado com sucesso")
    public ResponseEntity<Void> unarchiveParent(@PathVariable UUID parentId) {
        parentService.unarchiveParent(parentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{parentId}")
    @Operation(operationId = "deleteParent", description = "Deleta um responsável por ID")
    @ApiResponse(responseCode = "204", description = "Responsável deletado com sucesso")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.noContent().build();
    }



}
