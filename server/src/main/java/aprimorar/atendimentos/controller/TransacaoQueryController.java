package aprimorar.atendimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.atendimentos.dto.TransacaoResponseDTO;
import aprimorar.atendimentos.service.TransacaoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import aprimorar.atendimentos.dto.TransacaoFiltroRequest;

@Tag(name = "Transacao", description = "Operacoes relacionadas a transacoes.")
@RestController
@RequestMapping("/transacoes")
public class TransacaoQueryController {
    private final TransacaoQueryService transacaoQueryService;

    public TransacaoQueryController(TransacaoQueryService transacaoQueryService) {
        this.transacaoQueryService = transacaoQueryService;
    }

    @GetMapping()
    @Operation(operationId = "getTransacoes", description = "Lista transacoes com paginacao, ordenacao e filtros opcionais.")
    @ApiResponse(responseCode = "200", description = "Pagina de transacoes retornada conforme os filtros informados.")
    public ResponseEntity<Page<TransacaoResponseDTO>> findAll(
        @ParameterObject Pageable pageable,
        @ParameterObject TransacaoFiltroRequest filtro) {
        return ResponseEntity.ok(transacaoQueryService.findAll(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getTransacaoById", description = "Consulta uma transacao especifica pelo ID.")
    @ApiResponse(responseCode = "200", description = "Transacao encontrada e retornada.")
    public TransacaoResponseDTO findById(@PathVariable Long id) {
        return transacaoQueryService.findById(id);
    }

}
