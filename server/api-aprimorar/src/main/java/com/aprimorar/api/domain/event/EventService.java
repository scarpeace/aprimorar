package com.aprimorar.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.StudentRepository;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

@Service
public class EventService {

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepo, StudentRepository studentRepo, EmployeeRepository employeeRepo, EventMapper eventMapper) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEvents(Pageable pageable) {

        Page<Event> eventPage = eventRepo.findAll(pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long eventId) {

        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        return eventMapper.convertToDto(event);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByEmployeeId(Pageable pageable, UUID employeeId) {

        Page<Event> eventPage = eventRepo.findAllByEmployeeEntityId(employeeId, pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByStudentId(Pageable pageable, UUID studentId) {
        Page<Event> eventPage = eventRepo.findAllByStudentEntityId(studentId, pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    /*
          ------------------------ INSERT METHODS ------------------------
     */

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {

        Event event = eventMapper.convertToEntity(eventRequestDTO);

        EventRules.validate(event);
        validateParticipantAvailability(
                event.getStudent(),
                event.getEmployee(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );

        Event savedEvent = eventRepo.save(event);

        return eventMapper.convertToDto(savedEvent);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO request) {
        Event event = eventMapper.convertToEntity(request);

        EventRules.validate(event);
        findEventOrThrow(id);
        validateParticipantsExistence(request.student(), request.employee());
        validateParticipantAvailability(
                request.student(),
                request.employee(),
                request.startDateTime(),
                request.endDateTime()
        );

        Event updatedEvent = eventRepo.save(event);

        return eventMapper.convertToDto(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Event findEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
    }

    private void validateParticipantAvailability(
            Student student,
            Employee employee,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        boolean studentConflict = eventRepo.studentHasConflictingEvent(student.getId(), startDateTime, endDateTime);
        if (studentConflict) {
            throw new EventScheduleConflictException(
                    "O estudante informado já possui evento no intervalo"
            );
        }

        boolean employeeConflict = eventRepo.employeeHasConflictingEvent(employee.getId(), startDateTime, endDateTime);
        if (employeeConflict) {
            throw new EventScheduleConflictException(
                    "O colaborador informado já possui evento no intervalo"
            );
        }
    }

    private void validateParticipantsExistence(
            Student student,
            Employee employee
    ) {
        boolean studentExists = studentRepo.existsById(student.getId());
        if (studentExists) {
            throw new StudentNotFoundException("Estudante com o ID informado não encontrado no banco de dados");
        }

        boolean employeeExists = employeeRepo.existsById(employee.getId());
        if (employeeExists) {
            throw new StudentNotFoundException("Colaborador com o ID informado não encontrado no banco de dados");
        }
    }

}








