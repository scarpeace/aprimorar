package aprimorar.financeiro.financeiro_aluno.cobrancas.web;


import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.financeiro.financeiro_aluno.cobrancas.service.CobrancaAlunoService;
import aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto.CobrancaAlunoFiltroRequest;
import aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto.CobrancaAlunoResponse;
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
@RequestMapping("/financeiro/cobrancas")
@Tag(
    name = "Cobranças de alunos",
    description = "APIs para gestão de cobranças de alunos"
)
@CommonProblemResponses
public class CobrancaAlunoController {

    private final CobrancaAlunoService cobrancaService;

    public CobrancaAlunoController(CobrancaAlunoService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @GetMapping
    @Operation(operationId = "buscarCobrancasAlunos", description = "Lista cobranças de alunos.")
    @ApiResponse(responseCode = "200", description = "Cobranças encontradas.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<CobrancaAlunoResponse>> buscarCobrancas(
        @ParameterObject @Valid CobrancaAlunoFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cobrancaService.buscarCobrancas(filtro, pageable));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarCobrancaAlunoPorId", description = "Busca uma cobrança de aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Cobrança encontrada.")
    @NotFoundProblemResponse
    public ResponseEntity<CobrancaAlunoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cobrancaService.buscarCobrancaPorId(id));
    }

}
