package aprimorar.atendimentos.web;

import aprimorar.atendimentos.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.application.AtendimentoMutationService;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    @Operation(operationId = "createAtendimento", description = "Cria um atendimento vinculando aluno e colaborador.")
    @ApiResponse(responseCode = "201", description = "Atendimento criado e retornado com os dados consolidados de aluno e colaborador.")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentoResponseDTO> createAtendimento(@RequestBody @Valid AtendimentoRequestDTO request) {
        AtendimentoResponseDTO created = atendimentoMutationService.createAtendimento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateAtendimento", description = "Atualiza dados cadastrais do atendimento.")
    @ApiResponse(responseCode = "200", description = "Atendimento atualizado e retornado com os dados atuais.")
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Atendimento não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentoResponseDTO> updateAtendimento(
        @PathVariable UUID id,
        @RequestBody @Valid AtendimentoRequestDTO request
    ) {
        return ResponseEntity.ok(atendimentoMutationService.updateAtendimento(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    @ApiResponse(
        responseCode = "404",
        description = "Atendimento não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<Void> deleteAtendimento(@PathVariable UUID id) {
        atendimentoMutationService.deleteAtendimento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-student-charge")
    @Operation(operationId = "toggleStudentAtendimentoCharge", description = "Alterna a baixa financeira do aluno para o atendimento.")
    @ApiResponse(responseCode = "200", description = "Status de cobranca do aluno atualizado e atendimento retornado.")
    @ApiResponse(
        responseCode = "404",
        description = "Atendimento não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentoResponseDTO> toggleStudentAtendimentoCharge(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoMutationService.toggleStudentCharge(id));
    }

    @PatchMapping("/{id}/toggle-employee-payment")
    @Operation(
        operationId = "toggleEmployeeAtendimentoPayment",
        description = "Alterna a baixa financeira do colaborador para o atendimento."
    )
    @ApiResponse(responseCode = "200", description = "Status de pagamento do colaborador atualizado e atendimento retornado.")
    @ApiResponse(
        responseCode = "404",
        description = "Atendimento não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentoResponseDTO> toggleEmployeeAtendimentoPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoMutationService.toggleEmployeePayment(id));
    }
}
