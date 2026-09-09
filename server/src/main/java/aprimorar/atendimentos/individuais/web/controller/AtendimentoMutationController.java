package aprimorar.atendimentos.individuais.web.controller;

import aprimorar.atendimentos.individuais.web.dto.AtendimentoRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoRecorrenteRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoResponse;
import aprimorar.common.models.ErrorResponse;
import aprimorar.atendimentos.individuais.AtendimentoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
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
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AtendimentoResponse> agendar(@RequestBody @Valid AtendimentoRequest request) {
        AtendimentoResponse created = atendimentoService.agendar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/recorrentes")
    @Operation(operationId = "agendarAtendimentosRecorrentes", description = "Cria atendimentos semanais até a data final informada.")
    @ApiResponse(responseCode = "201", description = "Atendimentos recorrentes agendados.")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<List<AtendimentoResponse>> agendarRecorrente(@RequestBody @Valid AtendimentoRecorrenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoService.agendarRecorrente(request));
    }

    @PatchMapping("/{id}/concluir")
    @Operation(operationId = "concluirAtendimento", description = "Muda o status de um atendimento para CONCLUIDO.")
    @ApiResponse(responseCode = "200", description = "Atendimento concluído e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> concluir(@PathVariable Long id) {
        atendimentoService.concluir(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
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

    @PatchMapping("/{id}/cancelar")
    @Operation(operationId = "cancelarAtendimento", description = "Muda o status de um atendimento para CANCELADO.")
    @ApiResponse(responseCode = "200", description = "Atendimento cancelado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponse> cancelar(@PathVariable Long id) {
        atendimentoService.cancelar(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "excluirAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable Long id) {
        atendimentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/togglePagamentoAluno")
    @Operation(operationId = "togglePagamentoAluno", description = "Alterna o pagamento do aluno.")
    @ApiResponse(responseCode = "200", description = "Pagamento do aluno alternado.")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AtendimentoResponse> togglePagamentoAluno(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.togglePagamentoAluno(id));
    }

    @PatchMapping("/{id}/toggleRepasseColaborador")
    @Operation(operationId = "toggleRepasseColaborador", description = "Alterna o repasse do colaborador.")
    @ApiResponse(responseCode = "200", description = "Repasse do colaborador alternado.")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AtendimentoResponse> toggleRepasseColaborador(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.toggleRepasseColaborador(id));
    }
}
