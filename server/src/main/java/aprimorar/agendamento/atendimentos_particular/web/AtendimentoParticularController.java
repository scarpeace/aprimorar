package aprimorar.agendamento.atendimentos_particular.web;

import aprimorar.agendamento.atendimentos_particular.service.AtendimentoParticularService;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AgendarAtendimentoParticularDTO;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtualizarAtendimentoParticularDTO;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtendimentoParticularFiltroRequest;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtendimentoParticularResponse;
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
@RequestMapping("/instituicao/atendimentos/particular")
@Tag(
    name = "Atendimentos particulares",
    description = "APIs para gestão de atendimentos particulares"
)
@CommonProblemResponses
@BadRequestProblemResponse
@ConflictProblemResponse
@NotFoundProblemResponse
public class AtendimentoParticularController {

    private final AtendimentoParticularService atendimentoService;

    public AtendimentoParticularController(
        AtendimentoParticularService atendimentoService
    ) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    @Operation(
        operationId = "agendarAtendimentoParticular",
        description = "Cria um atendimento particular."
    )
    @ApiResponse(responseCode = "201", description = "Atendimento agendado com sucesso.")
    public ResponseEntity<Void> agendar(
        @RequestBody @Valid AgendarAtendimentoParticularDTO dto
    ) {
        Long id = atendimentoService.agendar(dto);

        return ResponseEntity.created(URI.create("/instituicao/atendimentos/particular/" + id)).build();
    }

    @PutMapping("/{id}")
    @Operation(
        operationId = "atualizarAtendimentoParticular",
        description = "Atualiza um atendimento particular."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento atualizado com sucesso.")
    public ResponseEntity<Void> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid AtualizarAtendimentoParticularDTO dto
    ) {
        atendimentoService.atualizar(id, dto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/realizar")
    @Operation(
        operationId = "realizarAtendimentoParticular",
        description = "Marca um atendimento particular como realizado."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento realizado com sucesso.")
    public ResponseEntity<Void> realizar(@PathVariable Long id) {
        atendimentoService.realizar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(
        operationId = "cancelarAtendimentoParticular",
        description = "Cancela um atendimento particular e seus lançamentos pendentes."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento cancelado com sucesso.")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        atendimentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
        operationId = "buscarAtendimentosParticulares",
        description = "Lista atendimentos particulares."
    )
    @ApiResponse(responseCode = "200", description = "Atendimentos encontrados.")
    public ResponseEntity<Page<AtendimentoParticularResponse>> buscarAtendimentos(
        @ParameterObject @Valid AtendimentoParticularFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "dataHoraInicio", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(atendimentoService.buscarAtendimentos(filtro, pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        operationId = "buscarAtendimentoParticularPorId",
        description = "Busca um atendimento particular por ID."
    )
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado.")
    public ResponseEntity<AtendimentoParticularResponse> buscarAtendimentoPorId(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            atendimentoService.buscarAtendimentoPorId(id)
        );
    }
}
