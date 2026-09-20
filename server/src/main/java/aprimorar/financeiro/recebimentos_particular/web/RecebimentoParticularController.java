package aprimorar.financeiro.recebimentos_particular.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.recebimentos_particular.service.RecebimentoParticularService;
import aprimorar.financeiro.recebimentos_particular.web.dto.RecebimentoParticularDetalheResponse;
import aprimorar.financeiro.recebimentos_particular.web.dto.RecebimentoParticularFiltroRequest;
import aprimorar.financeiro.recebimentos_particular.web.dto.RecebimentoParticularResponse;
import aprimorar.financeiro.recebimentos_particular.web.dto.RegistrarRecebimentoParticularRequest;
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
    name = "Recebimentos particulares",
    description = "APIs para registrar, consultar e cancelar recebimentos de cobranças particulares"
)
@CommonProblemResponses
@ConflictProblemResponse
public class RecebimentoParticularController {

    private final RecebimentoParticularService recebimentoService;

    public RecebimentoParticularController(RecebimentoParticularService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @PostMapping
    @Operation(
        operationId = "registrarRecebimentoParticular",
        description = "Registra um recebimento para uma ou mais cobranças do mesmo aluno."
    )
    @ApiResponse(responseCode = "201", description = "Recebimento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarRecebimento(
        @RequestBody @Valid RegistrarRecebimentoParticularRequest request
    ) {
        UUID id = recebimentoService.registrarRecebimento(
            request.cobrancaIds(),
            request.dataRecebimento(),
            request.formaPagamento(),
            request.comprovanteUrl()
        );

        return ResponseEntity.created(
            URI.create("/financeiro/cobrancas/recebimentos/" + id)
        ).build();
    }

    @GetMapping
    @Operation(
        operationId = "buscarRecebimentosParticulares",
        description = "Lista recebimentos particulares com filtros e paginação."
    )
    @ApiResponse(responseCode = "200", description = "Recebimentos encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RecebimentoParticularResponse>> buscarRecebimentos(
        @ParameterObject @Valid RecebimentoParticularFiltroRequest filtro,
        @ParameterObject
        @PageableDefault(sort = "dataRecebimento", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            recebimentoService.buscarRecebimentos(filtro, pageable)
                .map(RecebimentoParticularResponse::toDto)
        );
    }

    @GetMapping("/{recebimentoId}")
    @Operation(
        operationId = "buscarRecebimentoParticularPorId",
        description = "Busca os detalhes de um recebimento particular pelo ID."
    )
    @ApiResponse(responseCode = "200", description = "Recebimento encontrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<RecebimentoParticularDetalheResponse> buscarPorId(
        @PathVariable UUID recebimentoId
    ) {
        return ResponseEntity.ok(
            RecebimentoParticularDetalheResponse.toDto(
                recebimentoService.buscarDetalhesPorId(recebimentoId)
            )
        );
    }

    @DeleteMapping("/{recebimentoId}")
    @Operation(
        operationId = "cancelarRecebimentoParticular",
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
