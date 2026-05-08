package com.aprimorar.api.domain.event.internal;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.api.EmployeeService;
import com.aprimorar.api.domain.employee.api.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.api.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.api.EventService;
import com.aprimorar.api.domain.event.api.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.api.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.api.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.api.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.api.exception.InvalidEventException;
import com.aprimorar.api.domain.event.internal.repository.EventRepository;
import com.aprimorar.api.domain.event.internal.repository.EventSpecifications;
import com.aprimorar.api.domain.finance.api.TransactionService;
import com.aprimorar.api.domain.student.api.StudentService;
import com.aprimorar.api.domain.student.api.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.api.exception.StudentNotFoundException;
import com.aprimorar.api.shared.PageDTO;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepo;
    private final ObjectProvider<StudentService> studentService;
    private final ObjectProvider<EmployeeService> employeeService;
    private final EventMapper eventMapper;
    private final TransactionService transactionService;
    private final Clock clock;

    public EventServiceImpl(
        EventRepository eventRepo,
        ObjectProvider<StudentService> studentService,
        ObjectProvider<EmployeeService> employeeService,
        EventMapper eventMapper,
        TransactionService transactionService,
        Clock clock
    ) {
        this.eventRepo = eventRepo;
        this.studentService = studentService;
        this.employeeService = employeeService;
        this.eventMapper = eventMapper;
        this.transactionService = transactionService;
        this.clock = clock;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO dto) {
        StudentResponseDTO studentResponse = findStudentResponseOrThrow(dto.studentId());
        EmployeeResponseDTO employeeResponse = findEmployeeResponseOrThrow(dto.employeeId());

        validateParticipantAvailability(studentResponse, employeeResponse, dto.startDate(), dto.duration(), null);

        Event event = new Event(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            studentResponse.id(),
            studentResponse.name(),
            employeeResponse.id(),
            employeeResponse.name(),
            Instant.now(clock)
        );
        Event savedEvent = eventRepo.save(event);
        transactionService.createEventTransactions(savedEvent.getId(), savedEvent.getPrice(), savedEvent.getPayment());
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
        UUID employeeId,
        Boolean hideCharged,
        Boolean hidePaid
    ) {
        Specification<Event> spec = Specification.where(EventSpecifications.withStartDateAfter(startDate))
            .and(EventSpecifications.withEndDateBefore(endDate))
            .and(EventSpecifications.withStudentId(studentId))
            .and(EventSpecifications.withEmployeeId(employeeId))
            .and(EventSpecifications.withStudentCharged(hideCharged != null && hideCharged ? false : null))
            .and(EventSpecifications.withEmployeePaid(hidePaid != null && hidePaid ? false : null));

        if (search != null && !search.trim().isEmpty()) {
            Specification<Event> searchSpec = Specification.where(EventSpecifications.contentContainsIgnoreCase(search))
                .or(EventSpecifications.withStudentIds(studentService.getObject().findIdsByNameContaining(search.trim())))
                .or(EventSpecifications.withEmployeeIds(employeeService.getObject().findIdsByNameContaining(search.trim())));
            spec = spec.and(searchSpec);
        }

        Page<Event> eventPage = eventRepo.findAll(spec, pageable);
        Page<EventResponseDTO> eventsDtoPage = mapEventPage(eventPage);

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
    public PageDTO<EventResponseDTO> getEventsByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        String studentName,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    ) {
        Specification<Event> spec = Specification.where(EventSpecifications.withEmployeeId(employeeId))
            .and(EventSpecifications.withEmployeePaid(hidePaid != null && hidePaid ? false : null));

        if (studentName != null && !studentName.trim().isEmpty()) {
            spec = spec.and(EventSpecifications.withStudentIds(studentService.getObject().findIdsByNameContaining(studentName.trim())));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(EventSpecifications.withStartDateAfter(startDate))
                       .and(EventSpecifications.withEndDateBefore(endDate));
        }

        Page<Event> eventPage = eventRepo.findAll(spec, pageable);

        Page<EventResponseDTO> eventsDtoPage = mapEventPage(eventPage);
        log.info(
            "Consulta de eventos do colaborador {} finalizada, {} registros encontrados.",
            employeeId,
            eventPage.getTotalElements()
        );

        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional(readOnly = true)
    public PageDTO<EventResponseDTO> getEventsByStudentId(
        Pageable pageable,
        UUID studentId,
        String search,
        Boolean hideCharged,
        Instant startDate,
        Instant endDate
    ) {
        Specification<Event> spec = Specification.where(EventSpecifications.withStudentId(studentId))
            .and(EventSpecifications.withStudentCharged(hideCharged != null && hideCharged ? false : null))
            .and(EventSpecifications.withStartDateAfter(startDate))
            .and(EventSpecifications.withEndDateBefore(endDate));

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(EventSpecifications.withEmployeeIds(employeeService.getObject().findIdsByNameContaining(search.trim())));
        }

        Page<Event> eventPage = eventRepo.findAll(spec, pageable);
        Page<EventResponseDTO> eventsDtoPage = mapEventPage(eventPage);

        log.info("Consulta de eventos do aluno {} finalizada, {} registros encontrados.", studentId, eventPage.getTotalElements());
        return new PageDTO<>(eventsDtoPage);
    }

    @Transactional
    public EventResponseDTO updateEvent(UUID id, EventRequestDTO dto) {
        Event event = findEventOrThrow(id);
        StudentResponseDTO studentResponse = findStudentResponseOrThrow(dto.studentId());
        EmployeeResponseDTO employeeResponse = findEmployeeResponseOrThrow(dto.employeeId());

        validateParticipantAvailability(studentResponse, employeeResponse, dto.startDate(), dto.duration(), event);

        event.update(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            studentResponse.id(),
            studentResponse.name(),
            employeeResponse.id(),
            employeeResponse.name(),
            Instant.now(clock)
        );
        transactionService.syncEventTransactions(event.getId(), event.getPrice(), event.getPayment());

        log.info("Evento {} atualizado com sucesso.", event.getTitle().toUpperCase());
        return eventMapper.convertToDto(event);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event foundEvent = findEventOrThrow(eventId);
        transactionService.deleteEventTransactions(foundEvent.getId());
        eventRepo.delete(foundEvent);
        log.info("Evento {} deletado com sucesso.", foundEvent.getTitle().toUpperCase());
    }

    @Transactional
    public EventResponseDTO toggleStudentCharge(UUID id) {
        Event event = findEventOrThrow(id);
        event.toggleStudentCharge(Instant.now(clock));
        transactionService.syncStudentCharge(event.getId(), event.getStudentChargeDate());
        log.info("Status da cobrança do aluno no evento {} atualizado.", event.getTitle());
        return eventMapper.convertToDto(event);
    }

    @Transactional
    public EventResponseDTO toggleEmployeePayment(UUID id) {
        Event event = findEventOrThrow(id);
        event.toggleEmployeePayment(Instant.now(clock));
        transactionService.syncEmployeePayment(event.getId(), event.getEmployeePaymentDate());
        log.info("Status do pagamento do colaborador no evento {} atualizado.", event.getTitle());
        return eventMapper.convertToDto(event);
    }

    @Transactional(readOnly = true)
    public long countByStudentId(UUID studentId) {
        return eventRepo.countByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate) {
        return eventRepo.countByStudentIdAndStartDateBetween(studentId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumChargedByStudentId(UUID studentId) {
        return eventRepo.sumChargedByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPendingByStudentId(UUID studentId) {
        return eventRepo.sumPendingByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumChargedByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate) {
        return eventRepo.sumChargedByStudentIdInPeriod(studentId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPendingByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate) {
        return eventRepo.sumPendingByStudentIdInPeriod(studentId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countByEmployeeId(UUID employeeId) {
        return eventRepo.countByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate) {
        return eventRepo.countByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPaidByEmployeeId(UUID employeeId) {
        return eventRepo.sumPaidByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumUnpaidByEmployeeId(UUID employeeId) {
        return eventRepo.sumUnpaidByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate) {
        return eventRepo.sumPaidByEmployeeIdInPeriod(employeeId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumUnpaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate) {
        return eventRepo.sumUnpaidByEmployeeIdInPeriod(employeeId, startDate, endDate);
    }

    @Transactional
    public void reassignStudentEventsToGhost(UUID studentId) {
        eventRepo.reassignStudentEventsToGhost(studentId, UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Transactional
    public void reassignEmployeeEventsToGhost(UUID employeeId) {
        eventRepo.reassignEmployeeEventsToGhost(employeeId, UUID.fromString("00000000-0000-4000-8000-000000000001"));
    }

    /* ----- Helper Methods ----- */
    private Event findEventOrThrow(UUID eventId) {
        return eventRepo.findById(eventId).orElseThrow(EventNotFoundException::new);
    }

    private Page<EventResponseDTO> mapEventPage(Page<Event> eventPage) {
        List<UUID> studentIds = eventPage.getContent().stream().map(Event::getStudentId).distinct().toList();
        List<UUID> employeeIds = eventPage.getContent().stream().map(Event::getEmployeeId).distinct().toList();
        Map<UUID, StudentResponseDTO> studentsById = studentService.getObject().findByIds(studentIds);
        Map<UUID, EmployeeResponseDTO> employeesById = employeeService.getObject().findByIds(employeeIds);

        return eventPage.map(event -> eventMapper.convertToDto(
            event,
            studentsById.get(event.getStudentId()),
            employeesById.get(event.getEmployeeId())
        ));
    }

    private StudentResponseDTO findStudentResponseOrThrow(UUID studentId) {
        if (!studentService.getObject().existsById(studentId)) {
            throw new StudentNotFoundException("Estudante com o ID informado não encontrado no banco de dados");
        }
        return studentService.getObject().findById(studentId);
    }

    private EmployeeResponseDTO findEmployeeResponseOrThrow(UUID employeeId) {
        if (!employeeService.getObject().existsById(employeeId)) {
            throw new EmployeeNotFoundException("Colaborador com o ID informado não encontrado no banco de dados");
        }
        return employeeService.getObject().getReferenceById(employeeId);
    }

    private void validateParticipantAvailability(
        StudentResponseDTO student,
        EmployeeResponseDTO employee,
        Instant startDate,
        Double duration,
        Event event
    ) {
        Instant endDate = Event.calculateEndDate(startDate, duration);

        if (student.archivedAt() != null) {
            throw new InvalidEventException("Evento não pode ter estudantes arquivados");
        }

        if (employee.archivedAt() != null) {
            throw new InvalidEventException("Evento não pode ter colaboradores arquivados");
        }

        UUID eventId = event != null ? event.getId() : null;

        if (eventRepo.studentHasConflictingEvent(student.id(), startDate, endDate, eventId)) {
            throw new EventScheduleConflictException("O estudante informado já possui um evento no intervalo");
        }

        if (eventRepo.employeeHasConflictingEvent(employee.id(), startDate, endDate, eventId)) {
            throw new EventScheduleConflictException("O colaborador informado já possui um evento no intervalo");
        }
    }
}
