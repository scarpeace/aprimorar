package aprimorar.atendimentos;

import java.util.UUID;

public record Atendimento(Long id, UUID alunoId, UUID colaboradorId) {
}
