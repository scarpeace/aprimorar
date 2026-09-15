package aprimorar.atendimentos.individuais.web.controller;

import aprimorar.atendimentos.individuais.service.AtendimentoIndividualQueryService;
import aprimorar.atendimentos.individuais.service.AtendimentoIndividualService;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.atendimentos.individuais.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.calendario.AtendimentoIndividualCalendarioResponse;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CancelarCobrancasIndividualRequest;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CobrancaIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CobrancaIndividualResponse;
import aprimorar.atendimentos.individuais.web.dto.cobranca.RegistrarPagamentoIndividualRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.CancelarRepasseIndividualRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.RegistrarRepasseIndividualRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseIndividualResponse;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atendimentos-individuais")
@Tag(
    name = "Atendimentos individuais",
    description = "APIs para gestão de atendimentos, cobranças e repasses individuais"
)
@CommonProblemResponses
public class AtendimentoIndividualController {

    private final AtendimentoIndividualService atendimentoService;
    private final AtendimentoIndividualQueryService queryService;

    public AtendimentoIndividualController(
        AtendimentoIndividualService atendimentoService,
        AtendimentoIndividualQueryService queryService
    ) {
        this.atendimentoService = atendimentoService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(operationId = "agendarAtendimentoIndividual", description = "Cria um atendimento individual.")
    @ApiResponse(responseCode = "201", description = "Atendimento agendado com sucesso.")
    @BadRequestProblemResponse
    public ResponseEntity<Void> agendar(@RequestBody @Valid AtendimentoIndividualRequest request) {
        Long id = atendimentoService.agendar(request);
        return ResponseEntity.created(URI.create("/atendimentos-individuais/" + id)).build();
    }

    @PatchMapping("/{id}")
    @Operation(operationId = "atualizarAtendimentoIndividual", description = "Atualiza um atendimento individual.")
    @ApiResponse(responseCode = "204", description = "Atendimento atualizado com sucesso.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid AtendimentoIndividualRequest request
    ) {
        atendimentoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "excluirAtendimentoIndividual", description = "Exclui um atendimento individual.")
    @ApiResponse(responseCode = "204", description = "Atendimento excluído com sucesso.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atendimentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(operationId = "buscarAtendimentosIndividuais", description = "Lista atendimentos individuais.")
    @ApiResponse(responseCode = "200", description = "Atendimentos encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<AtendimentoIndividualResponse>> buscarAtendimentos(
        @ParameterObject AtendimentoIndividualFiltroRequest filtro,
        @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(queryService.buscarAtendimentos(pageable, filtro));
    }

    @GetMapping("/calendario")
    @Operation(operationId = "buscarCalendarioAtendimentosIndividuais", description = "Lista atendimentos individuais para um intervalo de calendário.")
    @ApiResponse(responseCode = "200", description = "Eventos do calendário encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<List<AtendimentoIndividualCalendarioResponse>> buscarCalendario(
        @ParameterObject @Valid AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return ResponseEntity.ok(queryService.buscarCalendario(filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarAtendimentoIndividualPorId", description = "Busca um atendimento individual por ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<AtendimentoIndividualResponse> buscarAtendimentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.buscarAtendimentoPorId(id));
    }

    @GetMapping("/cobrancas")
    @Operation(operationId = "buscarCobrancasIndividuais", description = "Lista cobranças individuais.")
    @ApiResponse(responseCode = "200", description = "Cobranças encontradas.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<CobrancaIndividualResponse>> buscarCobrancas(
        @ParameterObject @Valid CobrancaIndividualFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(queryService.buscarCobrancas(filtro, pageable));
    }

    @GetMapping("/cobrancas/{id}")
    @Operation(operationId = "buscarCobrancaIndividualPorId", description = "Busca uma cobrança individual por ID.")
    @ApiResponse(responseCode = "200", description = "Cobrança encontrada.")
    @NotFoundProblemResponse
    public ResponseEntity<CobrancaIndividualResponse> buscarCobrancaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.buscarCobrancaPorId(id));
    }

    @PostMapping("/cobrancas/pagar")
    @Operation(operationId = "registrarPagamentoCobrancasIndividuais", description = "Registra o pagamento de cobranças individuais.")
    @ApiResponse(responseCode = "204", description = "Pagamento registrado.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarPagamento(
        @RequestBody @Valid RegistrarPagamentoIndividualRequest request
    ) {
        atendimentoService.registrarPagamento(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cobrancas/cancelar")
    @Operation(operationId = "cancelarPagamentoCobrancasIndividuais", description = "Cancela pagamentos de cobranças individuais.")
    @ApiResponse(responseCode = "204", description = "Pagamentos cancelados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarPagamento(
        @RequestBody @Valid CancelarCobrancasIndividualRequest request
    ) {
        atendimentoService.cancelarPagamento(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/repasses")
    @Operation(operationId = "buscarRepassesIndividuais", description = "Lista repasses individuais.")
    @ApiResponse(responseCode = "200", description = "Repasses encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<RepasseIndividualResponse>> buscarRepasses(
        @ParameterObject @Valid RepasseIndividualFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(queryService.buscarRepasses(filtro, pageable));
    }

    @GetMapping("/repasses/{id}")
    @Operation(operationId = "buscarRepasseIndividualPorId", description = "Busca um repasse individual por ID.")
    @ApiResponse(responseCode = "200", description = "Repasse encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<RepasseIndividualResponse> buscarRepassePorId(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.buscarRepassePorId(id));
    }

    @PostMapping("/repasses/pagar")
    @Operation(operationId = "registrarRepassesIndividuais", description = "Registra repasses individuais.")
    @ApiResponse(responseCode = "204", description = "Repasses registrados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> registrarRepasse(@RequestBody @Valid RegistrarRepasseIndividualRequest request) {
        atendimentoService.registrarRepasse(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/repasses/cancelar")
    @Operation(operationId = "cancelarRepassesIndividuais", description = "Cancela repasses individuais.")
    @ApiResponse(responseCode = "204", description = "Repasses cancelados.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelarRepasse(@RequestBody @Valid CancelarRepasseIndividualRequest request) {
        atendimentoService.cancelarRepasse(request);
        return ResponseEntity.noContent().build();
    }
}
