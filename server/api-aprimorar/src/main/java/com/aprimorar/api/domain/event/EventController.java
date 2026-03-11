package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;
import com.aprimorar.api.shared.MapperUtils;

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
    private final MapperUtils mapperUtils;


    public EventController(EventService eventService, MapperUtils mapperUtils) {
        this.eventService = eventService;
        this.mapperUtils = mapperUtils;
    }

    @Operation(summary = "Create EVENT", description = "Creates event with date, price, payment, student and employee data")
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {
        log.info("EventController::createEvent request body {}", mapperUtils.jsonAsString(createEventDto));
        EventResponseDTO response = eventService.createEvent(createEventDto);
        
        log.info("EventController::createEvent response body {}", mapperUtils.jsonAsString(response));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all events", description = "Retrieves all events from database with pagination")
    @GetMapping
     public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EventResponseDTO> events = eventService.getEvents(pageRequest);

        log.info("EventController:getEvents execution completed. Page: {}, Size: {}, Total Elements: {}", events.getTotalPages(), events.getSize(), events.getTotalElements());
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "List single event by ID", description = "Retrieves single event based on ID")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long eventId) {
        log.info("EventController::getEventById by id  {}", eventId);

        EventResponseDTO foundEvent = eventService.findById(eventId);

        log.info("EventController::getEventById by id  {} response {}", eventId, mapperUtils.jsonAsString(foundEvent));
        return ResponseEntity.ok(foundEvent);
    }

    @Operation(summary = "Update EVENT", description = "Partially updates event data")
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventDTO updateEventDto) {
        log.info("EventController::updateEvent by id started with ID  {}", eventId);

        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, updateEventDto);
        log.info("EventController::updateEvent by id  {} response {}", updatedEvent.id(), mapperUtils.jsonAsString(updatedEvent));

        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(summary = "Delete EVENT", description = "Deletes single event based on ID")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> archiveEvent(@PathVariable Long eventId) {
        log.info("EventController::archive event started with ID  {}", eventId);
        eventService.deleteEvent(eventId);
        log.info("EventController::archiveEvent by id  {}", eventId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all events by Employee", description = "Retrieves all events of a single employee by ID")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByEmployeeId(
            @PathVariable UUID employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EventResponseDTO> events = eventService.getEventsByEmployeeId(employeeId, pageRequest);

        log.info("EventController:getEventsByEmployeeId execution completed. Page: {}, Size: {}, Total Elements: {}", events.getTotalPages(), events.getSize(), events.getTotalElements());
        return ResponseEntity.ok(events);
    }

    @Operation(summary = "List all events by Student", description = "Retrieves all events of a single student by ID")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStudentId(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EventResponseDTO> events = eventService.getEventsByStudentId(studentId, pageRequest);

        log.info("EventController:getEventsByStudentId execution completed. Page: {}, Size: {}, Total Elements: {}", events.getTotalPages(), events.getSize(), events.getTotalElements());
        return ResponseEntity.ok(events);
    }

}
