package aprimorar.instituicao.atendimentos_individuais.web;


import aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.instituicao.atendimentos_individuais.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.instituicao.atendimentos_individuais.web.dto.calendario.AtendimentoIndividualCalendarioResponse;

import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.instituicao.atendimentos_individuais.service.AtendimentoIndividualService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instituicao/atendimentos")
@Tag(
    name = "Atendimentos individuais",
    description = "APIs para gestão de atendimentos individuais"
)
@CommonProblemResponses
public class AtendimentoIndividualController {

    private final AtendimentoIndividualService atendimentoService;

    public AtendimentoIndividualController(AtendimentoIndividualService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    @Operation(operationId = "agendarAtendimentoIndividual", description = "Cria um atendimento individual.")
    @ApiResponse(responseCode = "201", description = "Atendimento agendado com sucesso.")
    @BadRequestProblemResponse
    public ResponseEntity<Void> agendar(@RequestBody @Valid AtendimentoIndividualRequest request) {
        Long id = atendimentoService.agendar(request);
        return ResponseEntity.created(URI.create("/instituicao/atendimentos/" + id)).build();
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

    @PatchMapping("/{id}/realizar")
    @Operation(
        operationId = "realizarAtendimentoIndividual",
        description = "Marca um atendimento individual como realizado."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento realizado com sucesso.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> realizar(@PathVariable Long id) {
        atendimentoService.realizar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(
        operationId = "cancelarAtendimentoIndividual",
        description = "Cancela um atendimento individual e seus lançamentos pendentes."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento cancelado com sucesso.")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        atendimentoService.cancelar(id);
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
        return ResponseEntity.ok(atendimentoService.buscarAtendimentos(pageable, filtro));
    }

    @GetMapping("/calendario")
    @Operation(operationId = "buscarCalendarioAtendimentosIndividuais", description = "Lista atendimentos individuais para um intervalo de calendário.")
    @ApiResponse(responseCode = "200", description = "Eventos do calendário encontrados.")
    @BadRequestProblemResponse
    public ResponseEntity<List<AtendimentoIndividualCalendarioResponse>> buscarCalendario(
        @ParameterObject @Valid AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return ResponseEntity.ok(atendimentoService.buscarCalendario(filtro));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "buscarAtendimentoIndividualPorId", description = "Busca um atendimento individual por ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado.")
    @NotFoundProblemResponse
    public ResponseEntity<AtendimentoIndividualResponse> buscarAtendimentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.buscarAtendimentoPorId(id));
    }

}
