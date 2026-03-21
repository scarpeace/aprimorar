package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/events")
@Tag(name = "Event", description = "Event management APIs")
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
            @PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<EventResponseDTO> events = eventService.getEvents(pageable, search);
        return ResponseEntity.ok(events);
    }

    @Operation(
        operationId = "getEventById",
        summary = "Obter evento por ID",
        description = "Retorna um único evento com base no ID fornecido."
    )
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {

        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @Operation(
        operationId = "getEventsByEmployeeId",
        summary = "Listar eventos por funcionário",
        description = "Retorna todos os eventos de um único funcionário com base no ID fornecido."
    )
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByEmployeeId(@PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID employeeId) {

        Page<EventResponseDTO> events = eventService.getEventsByEmployeeId(pageable, employeeId);
        return ResponseEntity.ok(events);
    }

    @Operation(
        operationId = "getEventsByStudentId",
        summary = "Listar eventos por aluno",
        description = "Retorna todos os eventos de um único aluno com base no ID fornecido."
    )
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStudentId(@PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID studentId) {

        Page<EventResponseDTO> events = eventService.getEventsByStudentId(pageable, studentId);
        return ResponseEntity.ok(events);
    }

    @Operation(
        operationId = "updateEvent",
        summary = "Atualizar evento",
        description = "Atualiza totalmente os dados de um evento."
    )
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
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }


}
