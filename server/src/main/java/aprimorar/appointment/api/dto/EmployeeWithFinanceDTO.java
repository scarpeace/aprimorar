package aprimorar.appointment.api.dto;

import aprimorar.shared.enums.Duty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EmployeeWithFinanceDTO(
    UUID id,
    String name,
    String cpf,
    String contact,
    Duty duty,
    Instant createdAt,
    boolean active,
    BigDecimal totalPaid,
    BigDecimal totalPending
) {}
