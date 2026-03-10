package com.aprimorar.api.domain.event;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.event.dto.CreateEventDTO;
import com.aprimorar.api.domain.event.dto.EventFilter;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;
import com.aprimorar.api.shared.PageQuery;
import com.aprimorar.api.shared.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/events")
@Tag(name = "Events", description = "Event management APIs")
public class EventController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("startDateTime", "endDateTime", "createdAt", "updatedAt");

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "List all EVENTS and/or Filtered", description = "Retrieves all events from database with pagination and filters if needed")
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> listEvents(
            @Valid @ModelAttribute PageQuery pageQuery,
            @ModelAttribute EventFilter eventFilter
    ) {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "startDateTime", ALLOWED_SORT_FIELDS);
        Page<EventResponseDTO> allEvents = eventService.listEvents(pageable, eventFilter);
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

    @Operation(summary = "Update EVENT", description = "Partially updates event data")
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventDTO updateEventDto) {
        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, updateEventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(summary = "Delete EVENT", description = "Deletes single event based on ID")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
