package com.aprimorar.api.domain.event.api;

import com.aprimorar.api.domain.event.api.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.api.dto.EventResponseDTO;
import com.aprimorar.api.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO dto);

    PageDTO<EventResponseDTO> getEvents(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        UUID studentId,
        UUID employeeId,
        Boolean hideCharged,
        Boolean hidePaid
    );

    EventResponseDTO findById(UUID eventId);

    PageDTO<EventResponseDTO> getEventsByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        String studentName,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    );

    PageDTO<EventResponseDTO> getEventsByStudentId(
        Pageable pageable,
        UUID studentId,
        String search,
        Boolean hideCharged,
        Instant startDate,
        Instant endDate
    );

    EventResponseDTO updateEvent(UUID id, EventRequestDTO dto);

    void deleteEvent(UUID eventId);

    EventResponseDTO toggleStudentCharge(UUID id);

    EventResponseDTO toggleEmployeePayment(UUID id);

    long countByStudentId(UUID studentId);

    long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate);

    BigDecimal sumChargedByStudentId(UUID studentId);

    BigDecimal sumPendingByStudentId(UUID studentId);

    BigDecimal sumChargedByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);

    BigDecimal sumPendingByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);

    long countByEmployeeId(UUID employeeId);

    long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate);

    BigDecimal sumPaidByEmployeeId(UUID employeeId);

    BigDecimal sumUnpaidByEmployeeId(UUID employeeId);

    BigDecimal sumPaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);

    BigDecimal sumUnpaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);

    void reassignStudentEventsToGhost(UUID studentId);

    void reassignEmployeeEventsToGhost(UUID employeeId);
}
