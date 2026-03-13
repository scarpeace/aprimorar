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
@Tag(name = "Events", description = "Event management APIs")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Create EVENT", description = "Creates event with date, price, payment, student and employee data")
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {

        EventResponseDTO response = eventService.createEvent(createEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all events", description = "Retrieves all events from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getEvents(@PageableDefault(page = 0, size = 20, sort = "startDateTime", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<EventResponseDTO> events = eventService.getEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "List single event by ID", description = "Retrieves single event based on ID")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {

        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @Operation(summary = "List all events by Employee", description = "Retrieves all events of a single employee by ID")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByEmployeeId(@PageableDefault(page = 0, size = 20, sort = "startDateTime", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID employeeId) {

        Page<EventResponseDTO> events = eventService.getEventsByEmployeeId(pageable, employeeId);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "List all events by Student ID", description = "Retrieves all events of a single student by ID")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStudentId(@PageableDefault(page = 0, size = 20, sort = "startDateTime", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID studentId) {

        Page<EventResponseDTO> events = eventService.getEventsByStudentId(pageable, studentId);
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Update EVENT", description = "Fully updates event data")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID eventId, @RequestBody @Valid EventRequestDTO eventRequestDTO) {

        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, eventRequestDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(summary = "Delete EVENT", description = "Deletes single event based on ID")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }


}
