package aprimorar.atendimentos.web;

import aprimorar.atendimentos.dto.AtendimentosAlunosKpisDTO;
import aprimorar.atendimentos.dto.AtendimentosColaboradorKpisDTO;
import aprimorar.atendimentos.dto.AtendimentosContentReportDTO;
import aprimorar.atendimentos.dto.AtendimentosKpisDTO;
import aprimorar.atendimentos.service.AtendimentoReportService;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/atendimentos/report")
@Tag(name = "Atendimento")
public class AtendimentoReportController {

    private final AtendimentoReportService atendimentoReportService;

    public AtendimentoReportController(AtendimentoReportService atendimentoReportService) {
        this.atendimentoReportService = atendimentoReportService;
    }

    @GetMapping
    @Operation(
        operationId = "getIndicadoresAtendimentos",
        description = "Consolida o financeiro institucional no periodo informado"
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro institucional retornado com totais consolidados.")
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
    public ResponseEntity<AtendimentosKpisDTO> getKpisAtendimentos(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoReportService.getKpisAtendimentos(startDate, endDate));
    }

    @GetMapping("/colaboradores")
    @Operation(
        operationId = "getOverviewFinanceiroColaboradores",
        description = "Lista colaboradores paginados com indicadores de pago e pendente por colaborador."
    )
    @ApiResponse(responseCode = "200", description = "Colaboradores retornados com indicadores de financeiro por linha e resumo consolidado.")
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
    public ResponseEntity<PageDTO<AtendimentosColaboradorKpisDTO>> getKpisAtendimentosColaboradores(
        @ParameterObject @PageableDefault(sort = "employeeName") Pageable pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            atendimentoReportService.getKpisAtendimentosColaboradores(pageable, startDate, endDate)
        );
    }

    @GetMapping("/alunos")
    @Operation(
        operationId = "getOverviewFinanceiroAlunos",
        description = "Lista alunos paginados com indicadores de cobrado e pendente por aluno."
    )
    @ApiResponse(responseCode = "200", description = "Alunos retornados com indicadores de financeiro por linha e resumo consolidado.")
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
    public ResponseEntity<PageDTO<AtendimentosAlunosKpisDTO>> getKpisAtendimentosAlunos(
        @ParameterObject @PageableDefault(sort = "studentName") Pageable pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            atendimentoReportService.getKpisAtendimentosAlunos(pageable, startDate, endDate)
        );
    }

    @GetMapping("/content")
    @Operation(
        operationId = "getAtendimentosContentReport",
        description = "Consulta o relatório de atendimentos por tipo de conteúdo."
    )
    @ApiResponse(responseCode = "200", description = "Relatório de atendimentos por tipo de conteúdo retornado com totais consolidados.")
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
    public ResponseEntity<AtendimentosContentReportDTO> getAtendimentosContentReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoReportService.getAtendimentosContentReport(startDate, endDate));
    }
}
