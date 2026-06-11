package aprimorar.atendimentos.controller;

import aprimorar.atendimentos.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.service.AtendimentoQueryService;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/atendimentos")
@Tag(name = "Atendimento")
public class AtendimentoQueryController {

    private final AtendimentoQueryService atendimentoQueryService;

    public AtendimentoQueryController(AtendimentoQueryService atendimentoQueryService) {
        this.atendimentoQueryService = atendimentoQueryService;
    }

    @GetMapping
    @Operation(
        operationId = "getAtendimentos",
        description = "Lista atendimentos com paginacao, ordenacao e filtros opcionais por texto, periodo, cobranca do aluno e pagamento do colaborador."
    )
    @ApiResponse(responseCode = "200", description = "Pagina de atendimentos retornada conforme os filtros informados.")
    @ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<PageDTO<AtendimentoResponseDTO>> getAtendimentos(
        @ParameterObject Pageable pageable,
        @ParameterObject AtendimentoFiltroRequest filtro
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentos(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAtendimentoById", description = "Consulta um atendimento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
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
    public ResponseEntity<AtendimentoResponseDTO> getAtendimentoById(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoQueryService.findAtendimentoById(id));
    }

    @GetMapping("/{id}/aluno")
    @Operation(
        operationId = "getAtendimentosByAluno",
        description = "Retorna a agenda paginada de um aluno junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Agenda e resumo financeiro do aluno retornados conforme periodo e filtros informados."
    )
    @ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Aluno não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentosAlunoResponseDTO> getAtendimentosByAluno(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant inicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fim,
        @RequestParam(required = false) Boolean cobrado
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentosByAluno(pageable, id, inicio, fim, cobrado));
    }

    @GetMapping("/{id}/colaborador")
    @Operation(
        operationId = "getAtendimentosByColaborador",
        description = "Retorna a agenda paginada de um colaborador junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Agenda e resumo financeiro do colaborador retornados conforme periodo e filtros informados."
    )
    @ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Colaborador não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<AtendimentosColaboradorResponseDTO> getAtendimentosByColaborador(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean ocultarPagos,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant inicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fim
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentosByColaborador(pageable, id, ocultarPagos, inicio, fim));
    }
}
