package aprimorar.atendimentos.dto;

import java.time.Instant;

public record AtendimentoFiltroRequest(
    String busca,
    Instant inicio,
    Instant fim,
    Boolean ocultarCobrados,
    Boolean ocultarPagos
) {
}
