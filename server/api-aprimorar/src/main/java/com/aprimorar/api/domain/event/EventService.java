package com.aprimorar.api.domain.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.aprimorar.api.domain.employee.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;
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

        EmployeeEntity employeeEntity = findEmployeeOrThrow(employeeId);
        Page<EventEntity> eventPage = eventRepo.findAllByEmployeeEntityId(employeeEntity.getId(), pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByStudentId(Pageable pageable, UUID studentId) {

        StudentEntity student = findStudentOrThrow(studentId);
        Page<EventEntity> eventPage = eventRepo.findAllByStudentEntityId(student.getId(), pageable);
        return eventPage.map(eventMapper::convertToDto);
    }

    /*
          ------------------------ INSERT METHODS METHODS ------------------------
     */

    //TODO adicionar mais regras de validação de datas (Não pode evento duplicado e etc)
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {

        EventEntity eventEntity = eventMapper.convertToEntity(eventRequestDTO);

        StudentEntity student = studentRepo.findById(eventRequestDTO.studentId()).orElseThrow(StudentNotFoundException::new);

        EmployeeEntity employee = employeeRepo.findById(eventRequestDTO.employeeId()).orElseThrow(EmployeeNotFoundException::new);

        eventEntity.assignParticipants(student, employee);
        eventEntity.validateDates(LocalDateTime.now());
        eventEntity.setCreatedAt(Instant.now());

        EventEntity savedEventEntity = eventRepo.save(eventEntity);

        return eventMapper.convertToDto(savedEventEntity);
    }

    //TODO: Tranformar isso em um PUT ao invés de PATCH
    @Transactional
    public EventResponseDTO updateEvent(Long eventId, UpdateEventDTO updateEventDto) {
        EventEntity foundEventEntity = findEventOrThrow(eventId);
        StudentEntity newStudent = findStudentOrThrow(updateEventDto.studentId());
        EmployeeEntity newEmployee = findEmployeeOrThrow(updateEventDto.employeeId());

        foundEventEntity.setTitle(updateEventDto.title());
        foundEventEntity.setDescription(updateEventDto.description());
        foundEventEntity.setStartDateTime(updateEventDto.startDateTime());
        foundEventEntity.setEndDateTime(updateEventDto.endDateTime());
        foundEventEntity.setPrice(updateEventDto.price());
        foundEventEntity.setPayment(updateEventDto.payment());
        foundEventEntity.setContent(updateEventDto.content());

        foundEventEntity.assignParticipants(newStudent, newEmployee);
        foundEventEntity.validateDates(LocalDateTime.now());
        foundEventEntity.setUpdatedAt(Instant.now());

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

}
