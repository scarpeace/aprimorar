package aprimorar.atendimentos.dto;

import java.time.Instant;
import java.util.UUID;

public record AtendimentoFiltroRequest(
    String busca,
    Instant inicio,
    Instant fim,
    Boolean ocultarCobrados,
    Boolean ocultarPagos,
    UUID alunoId,
    String alunoNome,
    UUID colaboradorId,
    String colaboradorNome
) {
}
