package com.aprimorar.api.domain.event.web;

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

import com.aprimorar.api.domain.event.EventService;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/events")
public class EventController implements EventControllerDocs {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {

        EventResponseDTO response = eventService.createEvent(createEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getEvents(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<EventResponseDTO> events = eventService.getEvents(pageable, search);
        return ResponseEntity.ok(events);
    }

    @Override
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID eventId) {

        EventResponseDTO foundEvent = eventService.findById(eventId);
        return ResponseEntity.ok(foundEvent);
    }

    @Override
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStudent(
            @ParameterObject Pageable pageable,
            @PathVariable UUID studentId) {

        Page<EventResponseDTO> events = eventService.getEventsByStudentId(pageable, studentId);
        return ResponseEntity.ok(events);
    }

    @Override
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByEmployee(
            @ParameterObject Pageable pageable,
            @PathVariable UUID employeeId) {

        Page<EventResponseDTO> events = eventService.getEventsByEmployeeId(pageable, employeeId);
        return ResponseEntity.ok(events);
    }

    @Override
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID eventId,
            @RequestBody @Valid EventRequestDTO eventRequestDTO) {

        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, eventRequestDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @Override
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
