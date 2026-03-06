package com.aprimorar.api.service;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public EventResponseDTO findById(Long eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        return eventMapper.toDto(foundEvent);
    }

    public EventResponseDTO createEvent(CreateEventDTO createEventDto) {
        log.info("Creating event for student: {} with employee: {}",
                createEventDto.studentId(), createEventDto.employeeId());

        Student student = findStudentOrThrow(createEventDto.studentId());
        Employee employee = findEmployeeOrThrow(createEventDto.employeeId());

        Event newEvent = eventMapper.toEntity(createEventDto);
        newEvent.setStudent(student);
        newEvent.setEmployee(employee);

        Event savedEvent = eventRepo.save(newEvent);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long eventId, CreateEventDTO createEventDto) {
        Event foundEvent = findEventOrThrow(eventId);

        // Update scalar fields via mapper
        eventMapper.updateFromDto(createEventDto, foundEvent);

        // Update student if changed
        if (createEventDto.studentId() != null
                && !createEventDto.studentId().equals(foundEvent.getStudent().getId())) {
            Student student = findStudentOrThrow(createEventDto.studentId());
            foundEvent.setStudent(student);
        }

        // Update employee if changed
        if (createEventDto.employeeId() != null
                && !createEventDto.employeeId().equals(foundEvent.getEmployee().getId())) {
            Employee employee = findEmployeeOrThrow(createEventDto.employeeId());
            foundEvent.setEmployee(employee);
        }

        return eventMapper.toDto(foundEvent);
    }

    public void deleteEvent(Long eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
    }

    private Event findEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo.findByIdAndArchivedAtIsNull(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findByIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }
}
