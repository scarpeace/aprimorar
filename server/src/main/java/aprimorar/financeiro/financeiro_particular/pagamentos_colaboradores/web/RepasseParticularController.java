package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.service.RepasseParticularService;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.RepasseParticularFiltroRequest;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto.RepasseParticularResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/financeiro/repasses/particular")
@Tag(
    name = "Repasses particulares",
    description = "APIs para consultar repasses de atendimentos particulares"
)
@CommonProblemResponses
public class RepasseParticularController {

    private final RepasseParticularService repasseService;

    public RepasseParticularController(RepasseParticularService repasseService) {
        this.repasseService = repasseService;
    }

    @GetMapping
    @Operation(
        operationId = "buscarRepassesParticulares",
        description = "Lista repasses particulares com filtros e paginação."
    )
    @ApiResponse(responseCode = "200", description = "Repasses encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RepasseParticularResponse>> buscarRepasses(
        @ParameterObject @Valid RepasseParticularFiltroRequest filtro,
        @ParameterObject
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<RepasseParticularResponse> repasses = repasseService
            .getRepasses(filtro, pageable);

        return ResponseEntity.ok(repasses);
    }

    @GetMapping("/{repasseId}")
    @Operation(
        operationId = "buscarRepasseParticularPorId",
        description = "Busca um repasse particular por ID."
    )
    @ApiResponse(responseCode = "200", description = "Repasse encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<RepasseParticularResponse> buscarPorId(
        @PathVariable Long repasseId
    ) {
        RepasseParticularResponse repasse = repasseService
            .getRepassePorId(repasseId);

        return ResponseEntity.ok(repasse);
    }
}
