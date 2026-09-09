package aprimorar.pessoas;

import java.util.UUID;

public record Aluno(UUID id, String nome, String escola, boolean ativo) {
}
