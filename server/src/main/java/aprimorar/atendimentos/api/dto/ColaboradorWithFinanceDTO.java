package aprimorar.atendimentos.api.dto;

import aprimorar.pessoas.colaborador.api.contract.DutyEnum;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ColaboradorWithFinanceDTO(
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
