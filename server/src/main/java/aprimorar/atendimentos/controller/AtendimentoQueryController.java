package aprimorar.atendimentos.controller;

import aprimorar.atendimentos.dto.CalendarioAtendimentosRespose;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.RelatorioAtendimentosResponse;
import aprimorar.atendimentos.service.AtendimentoQueryService;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Operation(operationId = "getAtendimentos",description = "Lista atendimentos com paginacao, ordenacao e filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Pagina de atendimentos retornada conforme os filtros informados.")
    public ResponseEntity<Page<AtendimentoResponse>> getAtendimentos(
        @ParameterObject Pageable pageable,
        @ParameterObject AtendimentoFiltroRequest filtro
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentos(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAtendimentoById", description = "Consulta um atendimento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
    public ResponseEntity<AtendimentoResponse> getAtendimentoById(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoQueryService.findAtendimentoById(id));
    }

    @GetMapping("/calendario")
    @Operation(operationId = "getCalendarioAtendimentos", description = "Retorna o conteúdo para montar o calendário de atendimentos.")
    @ApiResponse(responseCode = "200", description = "Calendário retornado com sucesso.")
    public ResponseEntity<List<CalendarioAtendimentosRespose>> getCalendarioAtendimentos(@RequestParam YearMonth anoMes) {
        return ResponseEntity.ok(atendimentoQueryService.getCalendarioAtendimentos(anoMes));
    }

    @GetMapping("/relatorio")
    @Operation(operationId = "getRelatorioAtendimentos", description = "Retorna o conteúdo para montar o relatório.")
    @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso.")
    public ResponseEntity<RelatorioAtendimentosResponse> getAtendimentosRelatorio(@RequestParam YearMonth anoMes) {
        return ResponseEntity.ok(atendimentoQueryService.getRelatorioAtendimentos(anoMes));
    }

}
