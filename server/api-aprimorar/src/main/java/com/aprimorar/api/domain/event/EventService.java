package com.aprimorar.api.domain.event;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import com.aprimorar.api.domain.employee.EmployeeEntity;
import com.aprimorar.api.domain.event.command.EventCommand;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.domain.student.StudentRepository;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

@Service
public class EventService {


    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;
    private final Clock applicationClock;

    public EventService(EventRepository eventRepo, StudentRepository studentRepo, EmployeeRepository employeeRepo, EventMapper eventMapper, Clock applicationClock) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
        this.applicationClock = applicationClock;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEvents(Pageable pageable) {

        Page<EventEntity> eventPage = eventRepo.findAll(pageable);
        return eventPage.map(eventMapper::convertToDto);

    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long eventId) {

        EventEntity eventEntity = findEventOrThrow(eventId);
        return eventMapper.convertToDto(eventEntity);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByEmployeeId(Pageable pageable, UUID employeeId) {

        findEmployeeOrThrow(employeeId);
        Page<EventEntity> eventPage = eventRepo.findAllByEmployeeEntityId(employeeId, pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByStudentId(Pageable pageable, UUID studentId) {

        findStudentOrThrow(studentId);
        Page<EventEntity> eventPage = eventRepo.findAllByStudentEntityId(studentId, pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    /*
          ------------------------ INSERT METHODS ------------------------
     */

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        StudentEntity newStudent = findStudentOrThrow(eventRequestDTO.studentId());
        EmployeeEntity newEmployee = findEmployeeOrThrow(eventRequestDTO.employeeId());

        EventCommand command = eventMapper.convertToCommand(eventRequestDTO);

        validateParticipantAvailability(
                null,
                eventRequestDTO.studentId(),
                eventRequestDTO.employeeId(),
                command.startDateTime(),
                command.endDateTime()
        );

        EventEntity eventEntity = new EventEntity();
        eventEntity.create(command, newStudent, newEmployee, LocalDateTime.now(applicationClock));

        EventEntity savedEventEntity = eventRepo.save(eventEntity);

        return eventMapper.convertToDto(savedEventEntity);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long eventId, EventRequestDTO eventRequestDTO) {
        EventEntity foundEventEntity = findEventOrThrow(eventId);
        StudentEntity updatedStudent = findStudentOrThrow(eventRequestDTO.studentId());
        EmployeeEntity updatedEmployee = findEmployeeOrThrow(eventRequestDTO.employeeId());

        EventCommand command = eventMapper.convertToCommand(eventRequestDTO);

        validateParticipantAvailability(
                eventId,
                eventRequestDTO.studentId(),
                eventRequestDTO.employeeId(),
                command.startDateTime(),
                command.endDateTime()
        );

        foundEventEntity.update(command, updatedStudent, updatedEmployee, LocalDateTime.now(applicationClock));

        return eventMapper.convertToDto(foundEventEntity);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        EventEntity foundEvent = findEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private EventEntity findEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
    }

    private StudentEntity findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId).orElseThrow(StudentNotFoundException::new);
    }

    private EmployeeEntity findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
    }

    private void validateParticipantAvailability(
            Long eventId,
            UUID studentId,
            UUID employeeId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        boolean studentConflict = hasStudentConflict(eventId, studentId, startDateTime, endDateTime);
        if (studentConflict) {
            throw new EventScheduleConflictException(
                    "O estudante informado ja possui evento no intervalo"
            );
        }

        boolean employeeConflict = hasEmployeeConflict(eventId, employeeId, startDateTime, endDateTime);
        if (employeeConflict) {
            throw new EventScheduleConflictException(
                    "O colaborador informado ja possui evento no intervalo"
            );
        }
    }

    private boolean hasStudentConflict(
            Long eventId,
            UUID studentId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        if (eventId != null) {
            return eventRepo.existsStudentConflictForUpdate(eventId,studentId,startDateTime,endDateTime);
        }
        return eventRepo.existsStudentConflict(studentId,startDateTime,endDateTime);
    }

    private boolean hasEmployeeConflict(
            Long eventId,
            UUID employeeId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        if (eventId != null) {
            return eventRepo.existsEmployeeConflictForUpdate(eventId,employeeId,startDateTime,endDateTime);
        }
        return eventRepo.existsEmployeeConflict(employeeId,startDateTime,endDateTime);
    }

}
