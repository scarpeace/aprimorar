package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.CreateEventDto;
import com.aprimorar.api.controller.dto.EventResponseDto;
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

    public Page<EventResponseDto> listEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepo.findAll(pageable);
        return eventPage.map(eventMapper::toDto);
    }

    public EventResponseDto findById(Long eventId) {
        Event foundEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        return eventMapper.toDto(foundEvent);
    }

    public EventResponseDto createEvent(CreateEventDto createEventDto) {
        log.info("Creating event for student: {} with employee: {}",
                createEventDto.studentId(), createEventDto.employeeId());

        Student student = studentRepo.findById(createEventDto.studentId())
                .orElseThrow(() -> new StudentNotFoundException(createEventDto.studentId()));

        Employee employee = employeeRepo.findById(createEventDto.employeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(createEventDto.employeeId()));

        Event newEvent = eventMapper.toEntity(createEventDto);
        newEvent.setStudent(student);
        newEvent.setEmployee(employee);

        Event savedEvent = eventRepo.save(newEvent);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponseDto updateEvent(Long eventId, CreateEventDto createEventDto) {
        Event foundEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Update scalar fields via mapper
        eventMapper.updateFromDto(createEventDto, foundEvent);

        // Update student if changed
        if (createEventDto.studentId() != null
                && !createEventDto.studentId().equals(foundEvent.getStudent().getId())) {
            Student student = studentRepo.findById(createEventDto.studentId())
                    .orElseThrow(() -> new StudentNotFoundException(createEventDto.studentId()));
            foundEvent.setStudent(student);
        }

        // Update employee if changed
        if (createEventDto.employeeId() != null
                && !createEventDto.employeeId().equals(foundEvent.getEmployee().getId())) {
            Employee employee = employeeRepo.findById(createEventDto.employeeId())
                    .orElseThrow(() -> new EmployeeNotFoundException(createEventDto.employeeId()));
            foundEvent.setEmployee(employee);
        }

        return eventMapper.toDto(foundEvent);
    }

    public void deleteEvent(Long eventId) {
        Event foundEvent = eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        eventRepo.delete(foundEvent);
    }
}
