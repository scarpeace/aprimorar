package aprimorar.financeiro.repasses.web;

import aprimorar.financeiro.repasses.web.dto.RepasseLoteResponse;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.repasses.service.RepasseIndividualService;
import aprimorar.financeiro.repasses.web.dto.CancelarRepasseIndividualRequest;
import aprimorar.financeiro.repasses.web.dto.RegistrarRepasseIndividualRequest;
import aprimorar.financeiro.repasses.web.dto.RepasseIndividualFiltroRequest;
import aprimorar.financeiro.repasses.web.dto.RepasseIndividualResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atendimentos-individuais/repasses")
@Tag(
    name = "Repasses individuais",
    description = "APIs para gestão de repasses individuais"
)
@CommonProblemResponses
public class RepasseIndividualController {

    private final RepasseIndividualService repasseService;

    public RepasseIndividualController(RepasseIndividualService repasseService) {
        this.repasseService = repasseService;
    }

    @GetMapping
    @Operation(operationId = "buscarRepassesIndividuais", description = "Lista repasses individuais.")
    @ApiResponse(responseCode = "200", description = "Repasses encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RepasseIndividualResponse>> buscarRepasses(
        @ParameterObject @Valid RepasseIndividualFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(repasseService.buscarRepasses(filtro, pageable));
    }

    @GetMapping("/lotes")
    @Operation(
        operationId = "buscarLotesDeRepasse",
        description = "Lista os repasses pagos agrupados por lote."
    )
    @ApiResponse(responseCode = "200", description = "Lotes de repasse encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RepasseLoteResponse>> buscarLotes(
        @RequestParam UUID colaboradorId,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(repasseService.buscarLotes(colaboradorId, pageable));
    }

    @GetMapping("/lotes/{loteId}")
    @Operation(
        operationId = "buscarLoteDeRepassePorId",
        description = "Busca um lote de repasses pelo identificador."
    )
    @ApiResponse(responseCode = "200", description = "Lote de repasses encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<RepasseLoteResponse> buscarLotePorId(@PathVariable UUID loteId) {
        return ResponseEntity.ok(repasseService.buscarLotePorId(loteId));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarRepasseIndividualPorId", description = "Busca um repasse individual por ID.")
    @ApiResponse(responseCode = "200", description = "Repasse encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<RepasseIndividualResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(repasseService.buscarPorId(id));
    }

    @PostMapping("/pagar")
    @Operation(operationId = "registrarRepassesIndividuais", description = "Registra repasses individuais.")
    @ApiResponse(responseCode = "204", description = "Repasses registrados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarRepasse(@RequestBody @Valid RegistrarRepasseIndividualRequest request) {
        repasseService.registrarRepasse(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancelar")
    @Operation(operationId = "cancelarRepassesIndividuais", description = "Cancela repasses individuais.")
    @ApiResponse(responseCode = "204", description = "Repasses cancelados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarRepasse(@RequestBody @Valid CancelarRepasseIndividualRequest request) {
        repasseService.cancelarRepasse(request);
        return ResponseEntity.noContent().build();
    }
}
