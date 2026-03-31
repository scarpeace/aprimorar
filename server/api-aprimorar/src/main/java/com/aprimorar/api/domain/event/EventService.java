package com.aprimorar.api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.event.repository.EventSpecifications;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.shared.PageDTO;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepo, StudentRepository studentRepo, EmployeeRepository employeeRepo,
            EventMapper eventMapper) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEvents(Pageable pageable, String search) {
        Page<Event> eventPage;
        if (search != null && !search.trim().isEmpty()) {
            Specification<Event> spec = EventSpecifications.searchContainsIgnoreCase(search.trim());
            eventPage = eventRepo.findAll(spec, pageable);
        } else {
            eventPage = eventRepo.findAll(pageable);
        }
        Page<EventResponseDTO> eventsDtoPage = eventPage.map(eventMapper::convertToDto);


        log.info("Consulta de eventos finalizada, {} registros encontrados.", eventPage.getTotalElements());
        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(UUID eventId) {

        Event event = eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
        log.info("Evento {} consultado com sucesso.", event.getTitle().toUpperCase());
        return eventMapper.convertToDto(event);
    }

    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEventsByEmployeeId(Pageable pageable, UUID employeeId) {

        Page<Event> eventPage = eventRepo.findAllByEmployeeId(employeeId, pageable);

        Page<EventResponseDTO> eventsDtoPage = eventPage.map(eventMapper::convertToDto);
        log.info(
                "Consulta de eventos do colaborador {} finalizada, {} registros encontrados.",
                employeeId,
                eventPage.getTotalElements());

        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEventsByStudentId(Pageable pageable, UUID studentId) {
        Page<Event> eventPage = eventRepo.findAllByStudentId(studentId, pageable);
        Page<EventResponseDTO> eventsDtoPage = eventPage.map(eventMapper::convertToDto);

        log.info(
                "Consulta de eventos do aluno {} finalizada, {} registros encontrados.",
                studentId,
                eventPage.getTotalElements());
        return new PageDTO<>(eventsDtoPage);
    }

    /* ----- Command Methods ----- */
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event event = eventMapper.convertToEntity(eventRequestDTO);

        Student student = resolveStudentOrThrow(eventRequestDTO.studentId());
        Employee employee = resolveEmployeeOrThrow(eventRequestDTO.employeeId());
        event.setStudent(student);
        event.setEmployee(employee);

        validateParticipantAvailability(
                student.getId(),
                employee.getId(),
                event.getStartDate(),
                event.getEndDateTime(),
                null);
        event.validateForCreation();

        Event savedEvent = eventRepo.save(event);
        log.info("Evento {} cadastrado com sucesso.", savedEvent.getTitle().toUpperCase());
        return eventMapper.convertToDto(savedEvent);
    }

    @Transactional
    public EventResponseDTO updateEvent(UUID id, EventRequestDTO request) {
        Event event = findEventOrThrow(id);
        
        // Verifica a janela de tempo com base no endDate original ANTES de modificar
        event.validateEditWindow();

        Student student = resolveStudentOrThrow(request.studentId());
        Employee employee = resolveEmployeeOrThrow(request.employeeId());

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartDate(eventMapper.toLocalDateTime(request.startDate()));
        event.setEndDateTime(eventMapper.toLocalDateTime(request.endDate()));
        event.setPrice(request.price());
        event.setPayment(request.payment());
        event.setContent(request.content());
        event.setStudent(student);
        event.setEmployee(employee);

        validateParticipantAvailability(
                student.getId(),
                employee.getId(),
                event.getStartDate(),
                event.getEndDateTime(),
                id);
        event.validateForUpdate();

        log.info("Evento {} atualizado com sucesso.", event.getTitle().toUpperCase());
        return eventMapper.convertToDto(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
        log.info("Evento {} deletado com sucesso.", foundEvent.getTitle().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Event findEventOrThrow(UUID eventId) {
        return eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
    }

    private Student resolveStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                "Estudante com o ID informado não encontrado no banco de dados"));
    }

    private Employee resolveEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(
                "Colaborador com o ID informado não encontrado no banco de dados"));
    }

    private void validateParticipantAvailability(
            UUID studentId,
            UUID employeeId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID ignoredEventId) {

        boolean studentConflict = eventRepo.studentHasConflictingEvent(studentId, startDate, endDate, ignoredEventId);
        if (studentConflict) {
            throw new EventScheduleConflictException(
                    "O estudante informado já possui evento no intervalo");
        }

        boolean employeeConflict = eventRepo.employeeHasConflictingEvent(employeeId, startDate, endDate, ignoredEventId);
        if (employeeConflict) {
            throw new EventScheduleConflictException(
                    "O colaborador informado já possui evento no intervalo");
        }

        if (studentRepo.existsByIdAndArchivedAtIsNotNull(studentId)) {
            throw new InvalidEventException(
                    "Evento não pode ter estudantes arquivados");
        }
        if (employeeRepo.existsByIdAndArchivedAtIsNotNull(employeeId)) {
            throw new InvalidEventException(
                    "Evento não pode ter colaboradores arquivados");
        }
    }

}
