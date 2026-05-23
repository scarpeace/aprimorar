package aprimorar.appointment.api.dto;

import aprimorar.registration.employee.api.contract.DutyEnum;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EmployeeWithFinanceDTO(
    UUID id,
    String name,
    String cpf,
    String contact,
    DutyEnum duty,
    Instant createdAt,
    boolean active,
    BigDecimal totalPaid,
    BigDecimal totalPending
) {}
