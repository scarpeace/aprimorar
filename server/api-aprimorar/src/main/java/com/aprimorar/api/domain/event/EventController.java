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
        PageDTO<EventResponseDTO> events = eventService.getEvents(pageable, search, startDate, endDate, status, studentId, employeeId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getEventById", description = "Retorna um evento por ID.")
    @ApiResponse(responseCode = "200", description = "Evento retornado com sucessso.")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        EventResponseDTO foundEvent = eventService.findById(id);
        return ResponseEntity.ok(foundEvent);
    }

    @GetMapping("/{id}/employee")
    @Operation(operationId = "getEventsByEmployeeId", description = "Retorna eventos por ID do colaborador.")
    @ApiResponse(responseCode = "200", description = "Página de eventos do colaborador retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventByEmployeeId(@ParameterObject Pageable pageable, @PathVariable UUID id) {
        PageDTO<EventResponseDTO> foundEvent = eventService.getEventsByEmployeeId(pageable, id);
        return ResponseEntity.ok(foundEvent);
    }

    @GetMapping("/{id}/student")
    @Operation(operationId = "getEventsByStudentId", description = "Retorna eventos por ID do aluno.")
    @ApiResponse(responseCode = "200", description = "Página de eventos do aluno retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventByStudentId(@ParameterObject Pageable pageable, @PathVariable UUID id) {
        PageDTO<EventResponseDTO> foundEvent = eventService.getEventsByStudentId(pageable, id);
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

    @PatchMapping("/{id}/complete")
    @Operation(operationId = "completeEvent", description = "Conclui o atendimento.")
    @ApiResponse(responseCode = "200", description = "Evento concluído com sucesso.")
    public ResponseEntity<EventResponseDTO> completeEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.completeEvent(id));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(operationId = "cancelEvent", description = "Cancela o atendimento.")
    @ApiResponse(responseCode = "200", description = "Evento cancelado com sucesso.")
    public ResponseEntity<EventResponseDTO> cancelEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.cancelEvent(id));
    }

    @PatchMapping("/{id}/reschedule")
    @Operation(operationId = "rescheduleEvent", description = "Re-agenda um evento cancelado.")
    @ApiResponse(responseCode = "200", description = "Evento re-agendado com sucesso.")
    public ResponseEntity<EventResponseDTO> rescheduleEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.rescheduleEvent(id));
    }

    @PatchMapping("/{id}/settle-income")
    @Operation(operationId = "settleIncomeEvent", description = "Dá baixa no recebimento (aluno) do evento.")
    @ApiResponse(responseCode = "200", description = "Baixa atualizada com sucesso.")
    public ResponseEntity<EventResponseDTO> settleIncomeEvent(@PathVariable UUID id, @RequestParam FinancialStatus status) {
        return ResponseEntity.ok(eventService.settleIncome(id, status));
    }

    @PatchMapping("/{id}/settle-expense")
    @Operation(operationId = "settleExpenseEvent", description = "Dá baixa no repasse (colaborador) do evento.")
    @ApiResponse(responseCode = "200", description = "Baixa atualizada com sucesso.")
    public ResponseEntity<EventResponseDTO> settleExpenseEvent(@PathVariable UUID id, @RequestParam FinancialStatus status) {
        return ResponseEntity.ok(eventService.settleExpense(id, status));
    }
}
