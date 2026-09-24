package aprimorar.financeiro.recebimentos_alunos.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.recebimentos_alunos.application.RecebimentoService;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoDetalheResponse;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoFiltroRequest;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RecebimentoResponse;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RegistrarRecebimentoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/cobrancas/recebimentos")
@Tag(
    name = "Recebimentos",
    description = "APIs para registrar, consultar e cancelar recebimentos de cobranças"
)
@CommonProblemResponses
@ConflictProblemResponse
class RecebimentosController {

    private final RecebimentoService recebimentoService;

    RecebimentosController(RecebimentoService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @PostMapping
    @Operation(
        operationId = "registrarRecebimento",
        description = "Registra um recebimento para uma ou mais cobranças do mesmo aluno."
    )
    @ApiResponse(responseCode = "201", description = "Recebimento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarRecebimento(
        @RequestBody @Valid RegistrarRecebimentoRequest request
    ) {
        UUID id = recebimentoService.registrarRecebimento(request);

        return ResponseEntity.created(
            URI.create("/financeiro/cobrancas/recebimentos/" + id)
        ).build();
    }

    @GetMapping
    @Operation(
        operationId = "getRecebimentos",
        description = "Lista recebimentos com filtros e paginação."
    )
    @ApiResponse(responseCode = "200", description = "Recebimentos encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RecebimentoResponse>> getRecebimentos(
        @ParameterObject @Valid RecebimentoFiltroRequest filtro,
        @ParameterObject
        @PageableDefault(sort = "dataRecebimento", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<RecebimentoResponse> recebimentos = recebimentoService
            .getRecebimentos(filtro, pageable);

        return ResponseEntity.ok(recebimentos);
    }

    @GetMapping("/{recebimentoId}")
    @Operation(
        operationId = "getRecebimentoById",
        description = "Busca os detalhes de um recebimento pelo ID."
    )
    @ApiResponse(responseCode = "200", description = "Recebimento encontrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<RecebimentoDetalheResponse> getRecebimentoById(
        @PathVariable UUID recebimentoId
    ) {
        RecebimentoDetalheResponse recebimento = recebimentoService
            .getRecebimentoPorId(recebimentoId);

        return ResponseEntity.ok(recebimento);
    }

    @DeleteMapping("/{recebimentoId}")
    @Operation(
        operationId = "cancelarRecebimento",
        description = "Cancela o recebimento inteiro e libera todas as cobranças vinculadas."
    )
    @ApiResponse(responseCode = "204", description = "Recebimento cancelado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarRecebimento(@PathVariable UUID recebimentoId) {
        recebimentoService.cancelarRecebimento(recebimentoId);
        return ResponseEntity.noContent().build();
    }
}
