package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.employee.entity.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.dto.CreateEventDTO;
import com.aprimorar.api.domain.event.dto.EventFilter;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.domain.student.StudentRepository;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

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
            EventFilter filter
    ) {
        Page<Event> eventPage = eventRepo.findAllWithFilter(
                filter.getStart(),
                filter.getEnd(),
                filter.getStudentId(),
                filter.getEmployeeId(),
                pageable
        );
        return eventPage.map(eventMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long eventId) {
        Event foundEvent = findAnyEventOrThrow(eventId);
        return eventMapper.toDto(foundEvent);
    }

    @Transactional
    public EventResponseDTO createEvent(CreateEventDTO createEventDto) {
        log.info("Creating event for studentId={} employeeId={}",
                createEventDto.studentId(), createEventDto.employeeId());

        StudentEntity studentEntity = findSchedulableStudentOrThrow(createEventDto.studentId());
        Employee employee = findSchedulableEmployeeOrThrow(createEventDto.employeeId());

        Event newEvent = eventMapper.toEntity(createEventDto);
        newEvent.setStudent(studentEntity);
        newEvent.setEmployee(employee);

        Event savedEvent = eventRepo.save(newEvent);
        return eventMapper.toDto(savedEvent);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long eventId, UpdateEventDTO updateEventDto) {
        Event foundEvent = findAnyEventOrThrow(eventId);
        log.info("Updating eventId={}", eventId);

        eventMapper.updateFromDto(updateEventDto, foundEvent);

        if (updateEventDto.studentId() != null
                && !updateEventDto.studentId().equals(foundEvent.getStudent().getId())) {
            StudentEntity studentEntity = findSchedulableStudentOrThrow(updateEventDto.studentId());
            foundEvent.setStudent(studentEntity);
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
        log.info("Deleting eventId={}", eventId);
        eventRepo.delete(foundEvent);
    }

    private Event findAnyEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private StudentEntity findSchedulableStudentOrThrow(UUID studentId) {
        return studentRepo.findByIdAndArchivedAtIsNull(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Employee findSchedulableEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findByIdAndArchivedAtIsNull(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId.toString()));
    }
}
