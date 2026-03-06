package com.aprimorar.api.service;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.entity.Event;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.EventContent;
import com.aprimorar.api.exception.domain.EmployeeNotFoundException;
import com.aprimorar.api.exception.domain.EventNotFoundException;
import com.aprimorar.api.exception.domain.StudentNotFoundException;
import com.aprimorar.api.mapper.EventMapper;
import com.aprimorar.api.repository.EmployeeRepository;
import com.aprimorar.api.repository.EventRepository;
import com.aprimorar.api.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private static final LocalDateTime VALID_START = LocalDateTime.of(2027, 6, 1, 10, 0);
    private static final LocalDateTime VALID_END   = LocalDateTime.of(2027, 6, 1, 11, 0);
    private static final String VALID_TITLE = "This is the first event of the month";
    private static final String VALID_DESCRIPTION = "This is the description of the test event";
    private static final BigDecimal VALID_PRICE = new BigDecimal("100.00");
    private static final BigDecimal VALID_PAYMENT = new BigDecimal("50.00");

    // ─── listEvents ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated list of events when success")
    void testListEvents() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("startDateTime"));

        Event event = new Event();
        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        Page<Event> eventPage = new PageImpl<>(List.of(event), pageable, 1);
        when(eventRepo.findAllWithFilters(null, null, null, null, pageable)).thenReturn(eventPage);
        when(eventMapper.toDto(event)).thenReturn(responseDto);

        Page<EventResponseDTO> result = eventService.listEvents(pageable, null, null, null, null);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(responseDto, result.getContent().getFirst());

        verify(eventRepo).findAllWithFilters(null, null, null, null, pageable);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepo, eventMapper);
    }

    @Test
    @DisplayName("Should return empty page when there are no events in database")
    void testEmptyEventList() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("startDateTime"));

        Page<Event> eventPage = new PageImpl<>(List.of(), pageable, 0);
        when(eventRepo.findAllWithFilters(null, null, null, null, pageable)).thenReturn(eventPage);

        Page<EventResponseDTO> result = eventService.listEvents(pageable, null, null, null, null);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(eventRepo).findAllWithFilters(null, null, null, null, pageable);
        verifyNoMoreInteractions(eventRepo);
        verifyNoInteractions(eventMapper);
    }

    @Test
    @DisplayName("Should apply date range filters when listing events")
    void testListEventsWithDateRangeFilter() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("startDateTime"));
        LocalDateTime start = LocalDateTime.of(2027, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2027, 6, 30, 23, 59);

        Event event = new Event();
        EventResponseDTO responseDto = mock(EventResponseDTO.class);
        Page<Event> eventPage = new PageImpl<>(List.of(event), pageable, 1);

        when(eventRepo.findAllWithFilters(start, end, null, null, pageable)).thenReturn(eventPage);
        when(eventMapper.toDto(event)).thenReturn(responseDto);

        Page<EventResponseDTO> result = eventService.listEvents(pageable, start, end, null, null);

        assertEquals(1, result.getTotalElements());
        assertSame(responseDto, result.getContent().getFirst());

        verify(eventRepo).findAllWithFilters(start, end, null, null, pageable);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepo, eventMapper);
    }

    @Test
    @DisplayName("Should apply student and employee filters when listing events")
    void testListEventsWithStudentAndEmployeeFilter() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("startDateTime"));
        UUID studentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        Event event = new Event();
        EventResponseDTO responseDto = mock(EventResponseDTO.class);
        Page<Event> eventPage = new PageImpl<>(List.of(event), pageable, 1);

        when(eventRepo.findAllWithFilters(null, null, studentId, employeeId, pageable)).thenReturn(eventPage);
        when(eventMapper.toDto(event)).thenReturn(responseDto);

        Page<EventResponseDTO> result = eventService.listEvents(pageable, null, null, studentId, employeeId);

        assertEquals(1, result.getTotalElements());
        assertSame(responseDto, result.getContent().getFirst());

        verify(eventRepo).findAllWithFilters(null, null, studentId, employeeId, pageable);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepo, eventMapper);
    }

    // ─── findById ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return EventResponseDto when event is found by id")
    void testFindByIdFound() {
        Long eventId = 1L;
        Event event = new Event();
        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(eventMapper.toDto(event)).thenReturn(responseDto);

        EventResponseDTO result = eventService.findById(eventId);

        assertSame(responseDto, result);

        verify(eventRepo).findById(eventId);
        verify(eventMapper).toDto(event);
        verifyNoMoreInteractions(eventRepo, eventMapper);
    }

    @Test
    @DisplayName("Should throw EventNotFoundException when event is not found by id")
    void testFindByIdNotFound() {
        Long eventId = 99L;
        when(eventRepo.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.findById(eventId));

        verify(eventRepo).findById(eventId);
        verifyNoMoreInteractions(eventRepo);
        verifyNoInteractions(eventMapper);
    }

    // ─── createEvent ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should create and return event when success")
    void testCreateEvent() {
        UUID studentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, employeeId);

        Student student = new Student();
        Employee employee = new Employee();
        Event newEvent = new Event();
        Event savedEvent = new Event();
        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        when(studentRepo.findByIdAndArchivedAtIsNull(studentId)).thenReturn(Optional.of(student));
        when(employeeRepo.findByIdAndActiveTrue(employeeId)).thenReturn(Optional.of(employee));
        when(eventMapper.toEntity(dto)).thenReturn(newEvent);
        when(eventRepo.save(newEvent)).thenReturn(savedEvent);
        when(eventMapper.toDto(savedEvent)).thenReturn(responseDto);

        EventResponseDTO result = eventService.createEvent(dto);

        assertSame(responseDto, result);
        assertSame(student, newEvent.getStudent());
        assertSame(employee, newEvent.getEmployee());

        verify(studentRepo).findByIdAndArchivedAtIsNull(studentId);
        verify(employeeRepo).findByIdAndActiveTrue(employeeId);
        verify(eventMapper).toEntity(dto);
        verify(eventRepo).save(newEvent);
        verify(eventMapper).toDto(savedEvent);
        verifyNoMoreInteractions(studentRepo, employeeRepo, eventRepo, eventMapper);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when student is not found during event creation")
    void testCreateEventStudentNotFound() {
        UUID studentId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, UUID.randomUUID());

        when(studentRepo.findByIdAndArchivedAtIsNull(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> eventService.createEvent(dto));

        verify(studentRepo).findByIdAndArchivedAtIsNull(studentId);
        verifyNoMoreInteractions(studentRepo);
        verifyNoInteractions(employeeRepo, eventMapper, eventRepo);
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when employee is not found during event creation")
    void testCreateEventEmployeeNotFound() {
        UUID studentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, employeeId);

        Student student = new Student();
        when(studentRepo.findByIdAndArchivedAtIsNull(studentId)).thenReturn(Optional.of(student));
        when(employeeRepo.findByIdAndActiveTrue(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> eventService.createEvent(dto));

        verify(studentRepo).findByIdAndArchivedAtIsNull(studentId);
        verify(employeeRepo).findByIdAndActiveTrue(employeeId);
        verifyNoMoreInteractions(studentRepo, employeeRepo);
        verifyNoInteractions(eventMapper, eventRepo);
    }

    // ─── updateEvent ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should update event without fetching student or employee when IDs are unchanged")
    void testUpdateEvent_sameIds() {
        Long eventId = 1L;
        UUID studentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, employeeId);

        Student student = studentWithId(studentId);
        Employee employee = employeeWithId(employeeId);
        Event foundEvent = eventWithParticipants(student, employee);

        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(foundEvent));
        when(eventMapper.toDto(foundEvent)).thenReturn(responseDto);

        EventResponseDTO result = eventService.updateEvent(eventId, dto);

        assertSame(responseDto, result);

        verify(eventRepo).findById(eventId);
        verify(eventMapper).updateFromDto(dto, foundEvent);
        verify(eventMapper).toDto(foundEvent);
        verifyNoMoreInteractions(eventRepo, eventMapper);
        verifyNoInteractions(studentRepo, employeeRepo);
    }

    @Test
    @DisplayName("Should update event and fetch new student when student ID changes")
    void testUpdateEvent_changedStudentId() {
        Long eventId = 1L;
        UUID newStudentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(newStudentId, employeeId);

        Student oldStudent = studentWithId(UUID.randomUUID());
        Employee employee = employeeWithId(employeeId);
        Student newStudent = studentWithId(newStudentId);
        Event foundEvent = eventWithParticipants(oldStudent, employee);

        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(foundEvent));
        when(studentRepo.findByIdAndArchivedAtIsNull(newStudentId)).thenReturn(Optional.of(newStudent));
        when(eventMapper.toDto(foundEvent)).thenReturn(responseDto);

        EventResponseDTO result = eventService.updateEvent(eventId, dto);

        assertSame(responseDto, result);
        assertSame(newStudent, foundEvent.getStudent());

        verify(eventRepo).findById(eventId);
        verify(eventMapper).updateFromDto(dto, foundEvent);
        verify(studentRepo).findByIdAndArchivedAtIsNull(newStudentId);
        verify(eventMapper).toDto(foundEvent);
        verifyNoMoreInteractions(eventRepo, eventMapper, studentRepo);
        verifyNoInteractions(employeeRepo);
    }

    @Test
    @DisplayName("Should update event and fetch new employee when employee ID changes")
    void testUpdateEvent_changedEmployeeId() {
        Long eventId = 1L;
        UUID studentId = UUID.randomUUID();
        UUID newEmployeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, newEmployeeId);

        Student student = studentWithId(studentId);
        Employee oldEmployee = employeeWithId(UUID.randomUUID());
        Employee newEmployee = employeeWithId(newEmployeeId);
        Event foundEvent = eventWithParticipants(student, oldEmployee);

        EventResponseDTO responseDto = mock(EventResponseDTO.class);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(foundEvent));
        when(employeeRepo.findByIdAndActiveTrue(newEmployeeId)).thenReturn(Optional.of(newEmployee));
        when(eventMapper.toDto(foundEvent)).thenReturn(responseDto);

        EventResponseDTO result = eventService.updateEvent(eventId, dto);

        assertSame(responseDto, result);
        assertSame(newEmployee, foundEvent.getEmployee());

        verify(eventRepo).findById(eventId);
        verify(eventMapper).updateFromDto(dto, foundEvent);
        verify(employeeRepo).findByIdAndActiveTrue(newEmployeeId);
        verify(eventMapper).toDto(foundEvent);
        verifyNoMoreInteractions(eventRepo, eventMapper, employeeRepo);
        verifyNoInteractions(studentRepo);
    }

    @Test
    @DisplayName("Should throw EventNotFoundException when event is not found during update")
    void testUpdateEvent_eventNotFound() {
        Long eventId = 99L;
        CreateEventDTO dto = validCreateEventDto(UUID.randomUUID(), UUID.randomUUID());

        when(eventRepo.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(eventId, dto));

        verify(eventRepo).findById(eventId);
        verifyNoMoreInteractions(eventRepo);
        verifyNoInteractions(studentRepo, employeeRepo, eventMapper);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when new student is not found during update")
    void testUpdateEvent_studentNotFound() {
        Long eventId = 1L;
        UUID newStudentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(newStudentId, employeeId);

        Student oldStudent = studentWithId(UUID.randomUUID());
        Employee employee = employeeWithId(employeeId);
        Event foundEvent = eventWithParticipants(oldStudent, employee);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(foundEvent));
        when(studentRepo.findByIdAndArchivedAtIsNull(newStudentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> eventService.updateEvent(eventId, dto));

        verify(eventRepo).findById(eventId);
        verify(eventMapper).updateFromDto(dto, foundEvent);
        verify(studentRepo).findByIdAndArchivedAtIsNull(newStudentId);
        verifyNoMoreInteractions(eventRepo, eventMapper, studentRepo);
        verifyNoInteractions(employeeRepo);
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when new employee is not found during update")
    void testUpdateEvent_employeeNotFound() {
        Long eventId = 1L;
        UUID studentId = UUID.randomUUID();
        UUID newEmployeeId = UUID.randomUUID();

        CreateEventDTO dto = validCreateEventDto(studentId, newEmployeeId);

        Student student = studentWithId(studentId);
        Employee oldEmployee = employeeWithId(UUID.randomUUID());
        Event foundEvent = eventWithParticipants(student, oldEmployee);

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(foundEvent));
        when(employeeRepo.findByIdAndActiveTrue(newEmployeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> eventService.updateEvent(eventId, dto));

        verify(eventRepo).findById(eventId);
        verify(eventMapper).updateFromDto(dto, foundEvent);
        verify(employeeRepo).findByIdAndActiveTrue(newEmployeeId);
        verifyNoMoreInteractions(eventRepo, eventMapper, employeeRepo);
        verifyNoInteractions(studentRepo);
    }

    // ─── deleteEvent ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should delete event when success")
    void testDeleteEvent() {
        Long eventId = 1L;
        Event event = new Event();

        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEvent(eventId);

        verify(eventRepo).findById(eventId);
        verify(eventRepo).delete(event);
        verifyNoMoreInteractions(eventRepo);
        verifyNoInteractions(studentRepo, employeeRepo, eventMapper);
    }

    @Test
    @DisplayName("Should throw EventNotFoundException when trying to delete a non-existent event")
    void testDeleteEventNotFound() {
        Long eventId = 99L;

        when(eventRepo.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(eventId));

        verify(eventRepo).findById(eventId);
        verifyNoMoreInteractions(eventRepo);
        verifyNoInteractions(studentRepo, employeeRepo, eventMapper);
    }

    private CreateEventDTO validCreateEventDto(UUID studentId, UUID employeeId) {
        return new CreateEventDTO(
                VALID_TITLE,
                VALID_DESCRIPTION,
                VALID_START,
                VALID_END,
                VALID_PRICE,
                VALID_PAYMENT,
                EventContent.ENEM,
                studentId,
                employeeId
        );
    }

    private Student studentWithId(UUID studentId) {
        Student student = new Student();
        student.setId(studentId);
        return student;
    }

    private Employee employeeWithId(UUID employeeId) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        return employee;
    }

    private Event eventWithParticipants(Student student, Employee employee) {
        Event event = new Event();
        event.setStudent(student);
        event.setEmployee(employee);
        return event;
    }
}
