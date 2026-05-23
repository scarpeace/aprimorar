package aprimorar.pessoas.aluno.api.event;

import java.util.UUID;

/**
 * Evento disparado quando um estudante é removido do sistema.
 */
public record AlunoDeletedEvent(UUID studentId) {
}
