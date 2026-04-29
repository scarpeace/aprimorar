package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
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
        EventResponseDTO created = eventService.createEvent(eventRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(operationId = "getEvents", description = "Retorna uma lista paginada de eventos.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEvents(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate,
        @RequestParam(required = false) UUID studentId,
        @RequestParam(required = false) UUID employeeId
    ) {
        return ResponseEntity.ok(eventService.getEvents(pageable, search, startDate, endDate, studentId, employeeId));
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
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventByEmployeeId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) String studentName,
        @RequestParam(required = false) Boolean hidePaid,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(eventService.getEventsByEmployeeId(pageable, id, studentName, hidePaid, month, year));
    }

    @GetMapping("/{id}/student")
    @Operation(operationId = "getEventsByStudentId", description = "Retorna eventos por ID do aluno.")
    @ApiResponse(responseCode = "200", description = "Página de eventos do aluno retornada com sucesso.")
    public ResponseEntity<PageDTO<EventResponseDTO>> getEventByStudentId(@ParameterObject Pageable pageable, @PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventsByStudentId(pageable, id));
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

    @PatchMapping("/{id}/toggle-student-charge")
    @Operation(operationId = "toggleStudentEventCharge", description = "Dá baixa na cobrança do aluno no evento.")
    @ApiResponse(responseCode = "200", description = "Cobrança do aluno atualizada com sucesso.")
    public ResponseEntity<EventResponseDTO> toggleStudentEventCharge(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.toggleStudentCharge(id));
    }

    @PatchMapping("/{id}/toggle-employee-payment")
    @Operation(operationId = "toggleEmployeeEventPayment", description = "Dá baixa no pagamento do colaborador no evento.")
    @ApiResponse(responseCode = "200", description = "Pagamento do colaborador atualizado com sucesso.")
    public ResponseEntity<EventResponseDTO> toggleEmployeeEventPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.toggleEmployeePayment(id));
    }
}
