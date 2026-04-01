package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    private static final ZoneId APP_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final LocalDateTime EVENT_START = LocalDateTime.of(2026, 3, 25, 10, 0);
    private static final LocalDateTime EVENT_END = LocalDateTime.of(2026, 3, 25, 11, 0);

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

    @DisplayName("Command methods")
       private static EventRequestDTO request() {
        return new EventRequestDTO(
                "Evento atualizado",
                "Descrição de teste",
                EventContent.AULA,
                EVENT_START.atZone(APP_ZONE).toInstant(),
                EVENT_END.atZone(APP_ZONE).toInstant(),
                BigDecimal.valueOf(120),
                BigDecimal.valueOf(80),
                STUDENT_ID,
                EMPLOYEE_ID
        );
    }
}
