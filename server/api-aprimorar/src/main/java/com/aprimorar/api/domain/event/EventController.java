package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.shared.PageDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/events")
@Tag(name = "Events", description = "Events management APIs")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(operationId = "createEvent", description = "Cria um novo evento com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso.")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {
        EventResponseDTO response = eventService.createEvent(createEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getEvents", description = "Retorna uma lista paginada de eventos.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEvents(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived
    ) {
        return ResponseEntity.ok(eventService.getEvents(pageable, search));
    }

    @GetMapping("/{eventId}")
    @Operation(operationId = "getEventById", description = "Retorna um evendo por ID.")
    @ApiResponse(responseCode = "200", description = "Evento retornado com sucessso.")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {
        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @GetMapping("/student/{studentId}")
    @Operation(operationId = "getEventsByStudent", description = "Retorna uma lista de eventos por ID do aluno.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos do aluno retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventsByStudent(
        @ParameterObject Pageable pageable,
        @PathVariable UUID studentId
    ) {
        return ResponseEntity.ok(eventService.getEventsByStudentId(pageable, studentId));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(operationId = "getEventsByEmployee", description = "Retorna uma lista de eventos por ID do colaborador.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos do responsável retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventsByEmployee(
        @ParameterObject Pageable pageable,
        @PathVariable UUID employeeId
    ) {
        return ResponseEntity.ok(eventService.getEventsByEmployeeId(pageable, employeeId));
    }

    @PutMapping("/{eventId}")
        @Operation(operationId = "updateEvent", description = "Atualiza o evento com todos os dados informados.")
    @ApiResponse(responseCode = "200", description = "Evento autalizado com sucesso.")
public ResponseEntity<EventResponseDTO> updateEvent(
        @PathVariable UUID eventId,
        @RequestBody @Valid EventRequestDTO eventRequestDTO
    ) {
        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, eventRequestDTO);
        return ResponseEntity.ok(updatedEvent);
    }

//     @DeleteMapping("/{eventId}")
//         @Operation(operationId = "deleteEvent", description = "Deleta um evento baseado no ID")
//     @ApiResponse(responseCode = "204", description = "Lista de eventos do aluno retornada com sucesso.")
// public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
//         eventService.deleteEvent(eventId);
//         return ResponseEntity.noContent().build();
//     }
}
