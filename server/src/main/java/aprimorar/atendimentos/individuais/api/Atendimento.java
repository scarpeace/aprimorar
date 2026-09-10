package aprimorar.atendimentos.individuais.api;

import java.util.UUID;

public record Atendimento(Long id, UUID alunoId, UUID colaboradorId) {
}
