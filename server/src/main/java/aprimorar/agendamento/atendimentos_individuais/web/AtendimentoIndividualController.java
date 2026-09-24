package aprimorar.agendamento.atendimentos_individuais.web;

import aprimorar.agendamento.atendimentos_individuais.application.AtendimentoIndividualService;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AgendarAtendimentoIndividualRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtualizarAtendimentoIndividualRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtendimentoIndividualFiltroRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtendimentoIndividualResponse;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instituicao/atendimentos/individuais")
@Tag(
    name = "Atendimentos",
    description = "APIs para gestão de atendimentos individuais"
)
@CommonProblemResponses
@BadRequestProblemResponse
@ConflictProblemResponse
@NotFoundProblemResponse
class AtendimentoIndividualController {

    private final AtendimentoIndividualService atendimentoService;

    AtendimentoIndividualController(
        AtendimentoIndividualService atendimentoService
    ) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    @Operation(
        operationId = "agendarAtendimentoIndividual",
        description = "Cria um atendimento individual."
    )
    @ApiResponse(responseCode = "201", description = "Atendimento agendado com sucesso.")
    public ResponseEntity<Void> agendar(
        @RequestBody @Valid AgendarAtendimentoIndividualRequest dto
    ) {
        Long id = atendimentoService.agendar(dto);

        return ResponseEntity.created(URI.create("/instituicao/atendimentos/individuais/" + id)).build();
    }

    @PutMapping("/{atendimentoId}")
    @Operation(
        operationId = "atualizarAtendimentoIndividual",
        description = "Atualiza um atendimento individual."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento atualizado com sucesso.")
    public ResponseEntity<Void> atualizar(
        @PathVariable Long atendimentoId,
        @RequestBody @Valid AtualizarAtendimentoIndividualRequest dto
    ) {
        atendimentoService.atualizar(atendimentoId, dto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{atendimentoId}/realizar")
    @Operation(
        operationId = "realizarAtendimentoIndividual",
        description = "Marca um atendimento individual como realizado."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento realizado com sucesso.")
    public ResponseEntity<Void> realizar(@PathVariable Long atendimentoId) {
        atendimentoService.realizar(atendimentoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{atendimentoId}/cancelar")
    @Operation(
        operationId = "cancelarAtendimentoIndividual",
        description = "Cancela um atendimento individual e seus lançamentos pendentes."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento cancelado com sucesso.")
    public ResponseEntity<Void> cancelar(@PathVariable Long atendimentoId) {
        atendimentoService.cancelar(atendimentoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
        operationId = "getAtendimentosIndividuais",
        description = "Lista atendimentos individuais."
    )
    @ApiResponse(responseCode = "200", description = "Atendimentos encontrados.")
    public ResponseEntity<Page<AtendimentoIndividualResponse>> getAtendimentosIndividuais(
        @ParameterObject @Valid AtendimentoIndividualFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "dataHoraInicio", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(atendimentoService.buscarAtendimentos(filtro, pageable));
    }

    @GetMapping("/{atendimentoId}")
    @Operation(
        operationId = "getAtendimentoIndividualById",
        description = "Busca um atendimento individual por ID."
    )
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado.")
    public ResponseEntity<AtendimentoIndividualResponse> getAtendimentoIndividualById(
        @PathVariable Long atendimentoId
    ) {
        return ResponseEntity.ok(
            atendimentoService.buscarAtendimentoPorId(atendimentoId)
        );
    }
}
