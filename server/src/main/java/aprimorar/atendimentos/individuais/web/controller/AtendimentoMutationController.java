package aprimorar.atendimentos.individuais.web.controller;

import aprimorar.atendimentos.individuais.web.dto.AtendimentoRequest;
import aprimorar.atendimentos.individuais.service.AtendimentoMutationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/atendimentos")
@Tag(name = "Atendimento")
public class AtendimentoMutationController {

    private final AtendimentoMutationService atendimentoService;

    public AtendimentoMutationController(AtendimentoMutationService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    @Operation(operationId = "agendarAtendimento", description = "Cria um atendimento vinculando aluno e colaborador.")
    @ApiResponse(responseCode = "201", description = "Atendimento agendado com sucesso.")
    public ResponseEntity<Void> agendar(@RequestBody @Valid AtendimentoRequest request) {
        Long id = atendimentoService.agendar(request);
        return ResponseEntity.created(URI.create("/v1/atendimentos/" + id)).build();
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "updateAtendimento", description = "Atualiza um atendimento existente.")
    @ApiResponse(responseCode = "204", description = "Atendimento atualizado com sucesso.")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody AtendimentoRequest request
    ) {
        atendimentoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "excluirAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable Long id) {
        atendimentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
