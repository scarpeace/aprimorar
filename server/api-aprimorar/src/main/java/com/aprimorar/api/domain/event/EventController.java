package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.enums.EventStatus;
import com.aprimorar.api.enums.FinancialStatus;
import com.aprimorar.api.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/events")
@Tag(name = "Event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(operationId = "createEvent", description = "Cadastra um novo evento no sistema.")
    @ApiResponse(responseCode = "201", description = "Evento cadastrado com sucesso.")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO eventRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(eventRequestDTO));
    }

    @GetMapping
    @Operation(operationId = "getEvents", description = "Retorna uma lista paginada de eventos.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEvents(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) UUID employeeId
    ) {
        return ResponseEntity.ok(eventService.getEvents(pageable, search, startDate, endDate, status, studentId, employeeId));
    }

    @GetMapping("/{eventId}")
    @Operation(operationId = "getEventById", description = "Retorna um evendo por ID.")
    @ApiResponse(responseCode = "200", description = "Evento retornado com sucessso.")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {
        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateEvent", description = "Atualiza os dados de um evento.")
    @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso.")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventRequestDTO request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteEvent", description = "Deleta um evento do sistema.")
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
    }

    @PatchMapping("/{id}/income-status")
    @Operation(operationId = "updateIncomeStatus", description = "Atualiza o status financeiro de entrada do evento.")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso.")
    public ResponseEntity<EventResponseDTO> updateIncomeStatus(
            @PathVariable UUID id,
            @RequestParam FinancialStatus status
    ) {
        return ResponseEntity.ok(eventService.updateIncomeStatus(id, status));
    }

    @PatchMapping("/{id}/expense-status")
    @Operation(operationId = "updateExpenseStatus", description = "Atualiza o status financeiro de despesa do evento.")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso.")
    public ResponseEntity<EventResponseDTO> updateExpenseStatus(
            @PathVariable UUID id,
            @RequestParam FinancialStatus status
    ) {
        return ResponseEntity.ok(eventService.updateExpenseStatus(id, status));
    }
}
