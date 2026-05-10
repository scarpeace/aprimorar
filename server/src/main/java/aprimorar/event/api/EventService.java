package aprimorar.event.api;

import aprimorar.event.api.dto.EventRequestDTO;
import aprimorar.event.api.dto.EventResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO dto);

   PageDTO<EventResponseDTO> getEvents(Pageable pageable);

    EventResponseDTO findById(UUID eventId);

   PageDTO<EventResponseDTO> getEventsByEmployeeId(Pageable pageable,UUID employeeId);

   PageDTO<EventResponseDTO> getEventsByStudentId(Pageable pageable,UUID studentId);

    EventResponseDTO updateEvent(UUID id, EventRequestDTO dto);

    void deleteEvent(UUID eventId);

    EventResponseDTO toggleStudentCharge(UUID id);

    EventResponseDTO toggleEmployeePayment(UUID id);

    long countByStudentId(UUID studentId);

//    long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate);
//
//    BigDecimal sumChargedByStudentId(UUID studentId);
//
//    BigDecimal sumPendingByStudentId(UUID studentId);
//
//    BigDecimal sumChargedByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);
//
//    BigDecimal sumPendingByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);

    long countByEmployeeId(UUID employeeId);

//    long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate);
//
//    BigDecimal sumPaidByEmployeeId(UUID employeeId);
//
//    BigDecimal sumUnpaidByEmployeeId(UUID employeeId);
//
//    BigDecimal sumPaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);
//
//    BigDecimal sumUnpaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);

    void reassignStudentEventsToGhost(UUID studentId);

    void reassignEmployeeEventsToGhost(UUID employeeId);
}
