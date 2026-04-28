package com.aprimorar.api.domain.event;

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
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;
    private final Clock clock;

    public EventService(
        EventRepository eventRepo,
        StudentRepository studentRepo,
        EmployeeRepository employeeRepo,
        EventMapper eventMapper,
        Clock clock
    ) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
        this.clock = clock;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO dto) {
        Student student = findStudentOrThrow(dto.studentId());
        Employee employee = findEmployeeOrThrow(dto.employeeId());

        Instant calculatedEndDate = dto.startDate().plus((long) (dto.duration() * 60), ChronoUnit.MINUTES);
        validateParticipantAvailability(student, employee, dto.startDate(), calculatedEndDate, null);

        Event event = new Event(
            dto.description(),
            dto.startDate(),
            calculatedEndDate,
            dto.payment(),
            dto.price(),
            dto.content(),
            student,
            employee,
            Instant.now(clock)
        );
        Event savedEvent = eventRepo.save(event);
        log.info("Evento {} cadastrado com sucesso.", savedEvent.getTitle().toUpperCase());
        return eventMapper.convertToDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEvents(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        UUID studentId,
        UUID employeeId
    ) {
        Specification<Event> spec = Specification.where(EventSpecifications.searchContainsIgnoreCase(search))
            .and(EventSpecifications.withStartDateAfter(startDate))
            .and(EventSpecifications.withEndDateBefore(endDate))
            .and(EventSpecifications.withStudentId(studentId))
            .and(EventSpecifications.withEmployeeId(employeeId));

        Page<Event> eventPage = eventRepo.findAll(spec, pageable);
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
    public PageDTO<EventResponseDTO> getEventsByEmployeeId(Pageable pageable, UUID employeeId, String studentName) {
        Specification<Event> spec = Specification.where(EventSpecifications.withEmployeeId(employeeId))
            .and(EventSpecifications.withStudentNameIgnoreCase(studentName));

        Page<Event> eventPage = eventRepo.findAll(spec, pageable);

        Page<EventResponseDTO> eventsDtoPage = eventPage.map(eventMapper::convertToDto);
        log.info("Consulta de eventos do colaborador {} finalizada, {} registros encontrados.", employeeId, eventPage.getTotalElements());

        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEventsByStudentId(Pageable pageable, UUID studentId) {
        Page<Event> eventPage = eventRepo.findAllByStudentId(studentId, pageable);
        Page<EventResponseDTO> eventsDtoPage = eventPage.map(eventMapper::convertToDto);

        log.info("Consulta de eventos do aluno {} finalizada, {} registros encontrados.", studentId, eventPage.getTotalElements());
        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional
    public EventResponseDTO updateEvent(UUID id, EventRequestDTO dto) {
        Event event = findEventOrThrow(id);
        Student student = findStudentOrThrow(dto.studentId());
        Employee employee = findEmployeeOrThrow(dto.employeeId());

        Instant calculatedEndDate = dto.startDate().plus((long) (dto.duration() * 60), ChronoUnit.MINUTES);
        validateParticipantAvailability(student, employee, dto.startDate(), calculatedEndDate, id);

        event.update(
            dto.description(),
            dto.startDate(),
            calculatedEndDate,
            dto.payment(),
            dto.price(),
            dto.content(),
            student,
            employee,
            Instant.now(clock)
        );

        log.info("Evento {} atualizado com sucesso.", event.getTitle().toUpperCase());
        return eventMapper.convertToDto(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
        log.info("Evento {} deletado com sucesso.", foundEvent.getTitle().toUpperCase());
    }

    @Transactional
    public EventResponseDTO settleStudentCharge(UUID id, boolean charged) {
        Event event = findEventOrThrow(id);
        event.setStudentCharged(charged);
        log.info("Status da cobrança do aluno no evento {} atualizado para {}.", event.getTitle(), charged);
        return eventMapper.convertToDto(event);
    }

    @Transactional
    public EventResponseDTO settleEmployeePayment(UUID id, boolean paid) {
        Event event = findEventOrThrow(id);
        event.setEmployeePaid(paid);
        log.info("Status do pagamento do colaborador no evento {} atualizado para {}.", event.getTitle(), paid);
        return eventMapper.convertToDto(event);
    }

    /* ----- Helper Methods ----- */
    private Event findEventOrThrow(UUID eventId) {
        return eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo
            .findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("Estudante com o ID informado não encontrado no banco de dados"));
    }

    private Employee findEmployeeOrThrow(UUID employeeId) {
        return employeeRepo
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException("Colaborador com o ID informado não encontrado no banco de dados"));
    }

    private void validateParticipantAvailability(
        Student student,
        Employee employee,
        Instant startDate,
        Instant endDate,
        UUID ignoredEventId
    ) {
        if (studentRepo.existsByIdAndArchivedAtIsNotNull(student.getId())) {
            throw new InvalidEventException("Evento não pode ter estudantes arquivados");
        }

        if (employeeRepo.existsByIdAndArchivedAtIsNotNull(employee.getId())) {
            throw new InvalidEventException("Evento não pode ter colaboradores arquivados");
        }

        boolean studentConflict = eventRepo.studentHasConflictingEvent(student.getId(), startDate, endDate, ignoredEventId);

        if (studentConflict) {
            throw new EventScheduleConflictException("O estudante informado já possui um evento no intervalo");
        }

        boolean employeeConflict = eventRepo.employeeHasConflictingEvent(employee.getId(), startDate, endDate, ignoredEventId);
        if (employeeConflict) {
            throw new EventScheduleConflictException("O colaborador informado já possui um evento no intervalo");
        }
    }
}
