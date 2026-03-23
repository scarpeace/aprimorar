package com.aprimorar.api.domain.event.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.exception.ProblemDetailResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Event", description = "Event management APIs")
public interface EventControllerDocs {

    @Operation(
            operationId = "createEvent",
            summary = "Criar evento",
            description = "Cria um novo evento com os dados fornecidos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EventResponseDTO> createEvent(EventRequestDTO request);

    @Operation(
            operationId = "getEvents",
            summary = "Listar eventos",
            description = "Retorna todos os eventos do banco de dados com paginação."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos eventos com paginação",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Page<EventResponseDTO>> getEvents(
            @Parameter(description = "Página com informações de paginação") Pageable pageable,
            @Parameter(description = "Termo de busca") String search
    );

    @Operation(
            operationId = "getEventById",
            summary = "Listar evento por ID",
            description = "Retorna um evento específico com base no ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem do evento por ID",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EventResponseDTO> getEventById(
            @Parameter(description = "ID do evento") UUID eventId
    );

    @Operation(
            operationId = "getEventsByEmployee",
            summary = "Listar evento por ID",
            description = "Retorna um evento específico com base no ID do colaborador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem do evento por ID",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))})
    ResponseEntity<Page<EventResponseDTO>> getEventsByEmployee(
            @Parameter(description = "Página com informações de paginação") Pageable pageable,
            @Parameter(description = "ID do colaborador") UUID employeeId
    );

    @Operation(
            operationId = "getEventsByStudent",
            summary = "Listar evento por ID",
            description = "Retorna um evento específico com base no ID do aluno."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem do evento por ID do aluno",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Page<EventResponseDTO>> getEventsByStudent(
            @Parameter(description = "Página com informações de paginação") Pageable pageable,
            @Parameter(description = "ID do aluno") UUID studentId
    );

    @Operation(
            operationId = "updateEvent",
            summary = "Atualizar evento",
            description = "Atualiza totalmente os dados de um evento."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = EventResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<EventResponseDTO> updateEvent(
            @Parameter(description = "ID do evento") UUID eventId,
            @Parameter(description = "Dados do evento") EventRequestDTO eventRequestDto
    );

    @Operation(
            operationId = "deleteEvent",
            summary = "Deletar evento",
            description = "Deleta um evento com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> deleteEvent(UUID eventId);

}
