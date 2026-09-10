package aprimorar.atendimentos.individuais.api;

import java.math.BigDecimal;
import java.util.UUID;

public record AtendimentoCreatedEvent(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {}
