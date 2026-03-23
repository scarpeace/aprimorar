package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.annotations.ErrorResponsesAnnotation;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/events")
@Tag(name = "Event", description = "Event management APIs")
@ErrorResponsesAnnotation
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(
            operationId = "createEvent",
            summary = "Criar evento",
            description = "Cria um novo evento com os dados fornecidos."
    )
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {

        EventResponseDTO response = eventService.createEvent(createEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            operationId = "getEvents",
            summary = "Listar eventos",
            description = "Retorna todos os eventos do banco de dados com paginação."
    )
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<EventResponseDTO> events = eventService.getEvents(pageable, search);
        return ResponseEntity.ok(events);
    }

    @Operation(
            operationId = "getEventById",
            summary = "Obter evento por ID",
            description = "Retorna um único evento com base no ID fornecido."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {

        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @Operation(
            operationId = "getEventsByEmployee",
            summary = "Listar eventos por funcionário",
            description = "Retorna todos os eventos de um único funcionário com base no ID fornecido."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByEmployee(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID employeeId) {

        Page<EventResponseDTO> events = eventService.getEventsByEmployeeId(pageable, employeeId);
        return ResponseEntity.ok(events);
    }

    @Operation(
            operationId = "getEventsByStudent",
            summary = "Listar eventos por aluno",
            description = "Retorna todos os eventos de um único aluno com base no ID fornecido."
    )
    @ApiResponse(responseCode = "200")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStudent(@ParameterObject @PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID studentId) {

        Page<EventResponseDTO> events = eventService.getEventsByStudentId(pageable, studentId);
        return ResponseEntity.ok(events);
    }

    @Operation(
            operationId = "updateEvent",
            summary = "Atualizar evento",
            description = "Atualiza totalmente os dados de um evento."
    )
    @ApiResponse(responseCode = "200")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID eventId, @RequestBody @Valid EventRequestDTO eventRequestDTO) {

        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, eventRequestDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(
            operationId = "deleteEvent",
            summary = "Deletar evento",
            description = "Deleta um evento com base no ID fornecido."
    )
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
