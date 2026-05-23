package aprimorar.atendimentos.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AlunoWithFinanceDTO(
    UUID id,
    String name,
    String cpf,
    String contact,
    Integer age,
    String school,
    Instant createdAt,
    Boolean active,
    BigDecimal totalCharged,
    BigDecimal totalPending
) {}
