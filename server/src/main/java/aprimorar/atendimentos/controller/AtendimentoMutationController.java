package aprimorar.atendimentos.controller;

import aprimorar.atendimentos.dto.AtendimentoRequest;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import aprimorar.atendimentos.dto.ReagendarAtendimentoRequest;
import aprimorar.atendimentos.service.AtendimentoMutationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final AtendimentoMutationService atendimentoMutationService;

    public AtendimentoMutationController(AtendimentoMutationService atendimentoMutationService) {
        this.atendimentoMutationService = atendimentoMutationService;
    }

    @PostMapping
    @Operation(operationId = "agendarAtendimento", description = "Cria um atendimento vinculando aluno e colaborador.")
    @ApiResponse(responseCode = "201", description = "Atendimento agendado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> agendar(@RequestBody @Valid AtendimentoRequest request) {
        AtendimentoResponse created = atendimentoMutationService.agendar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/concluir")
    @Operation(operationId = "concluirAtendimento", description = "Muda o status de um atendimento para CONCLUIDO.")
    @ApiResponse(responseCode = "200", description = "Atendimento concluído e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> concluir(@PathVariable UUID id) {
        atendimentoMutationService.concluir(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/{id}/reagendar")
    @Operation(operationId = "reagendarAtendimento", description = "Reagenda um atendimento para uma nova data e hora.")
    @ApiResponse(responseCode = "200", description = "Atendimento reagendado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> reagendar(
        @PathVariable UUID id,
        @RequestBody ReagendarAtendimentoRequest request
    ) {
        atendimentoMutationService.reagendar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(operationId = "cancelarAtendimento", description = "Muda o status de um atendimento para CANCELADO.")
    @ApiResponse(responseCode = "200", description = "Atendimento cancelado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> cancelar(@PathVariable UUID id) {
        atendimentoMutationService.cancelar(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "excluirAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable UUID id) {
        atendimentoMutationService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
