package com.aprimorar.api.domain.dashboard.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;
import com.aprimorar.api.exception.ProblemResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Dashboard", description = "APIs do dashboard operacional")
public interface DashboardControllerDocs {

    @Operation(
            operationId = "getDashboardSummary",
            summary = "Obter resumo do dashboard",
            description = "Retorna KPIs mensais (alunos ativos, aulas, receita, custo), "
            + "distribuição por tipo de conteúdo e ponteiros de navegação para o mês anterior e próximo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resumo do dashboard obtido com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = DashboardSummaryResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemResponseDTO.class)))
    })
    ResponseEntity<DashboardSummaryResponseDTO> getDashboardSummary(
            @Parameter(description = "Ano do período (ex: 2026)", example = "2026")
            @RequestParam Integer year,
            @Parameter(description = "Mês do período (1-12)", example = "3")
            @RequestParam Integer month
    );

}
