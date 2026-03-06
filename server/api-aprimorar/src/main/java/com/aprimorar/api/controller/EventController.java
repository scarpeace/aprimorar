package com.aprimorar.api.controller;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.service.EventService;
import com.aprimorar.api.util.PageableUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/events")
@Tag(name = "Events", description = "Event management APIs")
public class EventController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("startDateTime", "endDateTime", "createdAt", "updatedAt");

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "List all EVENTS", description = "Retrieves all events from database with pagination")
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "startDateTime") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) UUID employeeId
    ) {
        Pageable pageable = PageableUtils.buildPageable(page, size, sortBy, "startDateTime", ALLOWED_SORT_FIELDS);
        Page<EventResponseDTO> allEvents = eventService.listEvents(pageable, start, end, studentId, employeeId);
        return ResponseEntity.ok(allEvents);
    }

    @Operation(summary = "List single EVENT", description = "Retrieves single event based on ID")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long eventId) {
        var foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @Operation(summary = "Create EVENT", description = "Creates event with date, price, payment, student and employee data")
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid CreateEventDTO createEventDto) {
        EventResponseDTO response = eventService.createEvent(createEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update EVENT", description = "Updates event with event data")
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid CreateEventDTO createEventDto) {
        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, createEventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(summary = "Delete EVENT", description = "Deletes single event based on ID")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
