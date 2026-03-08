package com.aprimorar.api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.dto.event.UpdateEventDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.entity.Event;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.exception.domain.EmployeeNotFoundException;
import com.aprimorar.api.exception.domain.EventNotFoundException;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.EventMapper;
import com.aprimorar.api.repository.EmployeeRepository;
import com.aprimorar.api.repository.EventRepository;
import com.aprimorar.api.repository.StudentRepository;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepo,
            StudentRepository studentRepo,
            EmployeeRepository employeeRepo,
            EventMapper eventMapper) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> listEvents(
            Pageable pageable,
            LocalDateTime start,
            LocalDateTime end,
            UUID studentId,
            UUID employeeId
    ) {
        Page<Event> eventPage = eventRepo.findAllWithFilters(start, end, studentId, employeeId, pageable);
        return eventPage.map(eventMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long eventId) {
        Event foundEvent = findAnyEventOrThrow(eventId);
        return eventMapper.toDto(foundEvent);
    }

    @Transactional
    public EventResponseDTO createEvent(CreateEventDTO createEventDto) {
        log.info("Creating event for student: {} with employee: {}",
                createEventDto.studentId(), createEventDto.employeeId());

        Student student = findSchedulableStudentOrThrow(createEventDto.studentId());
        Employee employee = findSchedulableEmployeeOrThrow(createEventDto.employeeId());

        Event newEvent = eventMapper.toEntity(createEventDto);
        newEvent.setStudent(student);
        newEvent.setEmployee(employee);

        Event savedEvent = eventRepo.save(newEvent);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long eventId, UpdateEventDTO updateEventDto) {
        Event foundEvent = findAnyEventOrThrow(eventId);

        eventMapper.updateFromDto(updateEventDto, foundEvent);

        if (updateEventDto.studentId() != null
                && !updateEventDto.studentId().equals(foundEvent.getStudent().getId())) {
            Student student = findSchedulableStudentOrThrow(updateEventDto.studentId());
            foundEvent.setStudent(student);
        }

        if (updateEventDto.employeeId() != null
                && !updateEventDto.employeeId().equals(foundEvent.getEmployee().getId())) {
            Employee employee = findSchedulableEmployeeOrThrow(updateEventDto.employeeId());
            foundEvent.setEmployee(employee);
        }

        return eventMapper.toDto(foundEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event foundEvent = findAnyEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
    }

    private Event findAnyEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Student findSchedulableStudentOrThrow(UUID studentId) {
        return studentRepo.findByIdAndArchivedAtIsNull(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Employee findSchedulableEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findByIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }
}
