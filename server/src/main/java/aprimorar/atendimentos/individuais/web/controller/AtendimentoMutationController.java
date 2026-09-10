package aprimorar.atendimentos.individuais.web.controller;

import aprimorar.atendimentos.individuais.web.dto.AtendimentoRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoResponse;
import aprimorar.atendimentos.individuais.service.AtendimentoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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

    private final AtendimentoServiceImpl atendimentoService;

    public AtendimentoMutationController(AtendimentoServiceImpl atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    @Operation(operationId = "agendarAtendimento", description = "Cria um atendimento vinculando aluno e colaborador.")
    @ApiResponse(responseCode = "201", description = "Atendimento agendado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> agendar(@RequestBody @Valid AtendimentoRequest request) {
        AtendimentoResponse created = atendimentoService.agendar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "updateAtendimento", description = "Atualiza um atendimento existente.")
    @ApiResponse(responseCode = "200", description = "Atendimento atualizado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> update(
        @PathVariable Long id,
        @RequestBody AtendimentoRequest request
    ) {
        atendimentoService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "excluirAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable Long id) {
        atendimentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
