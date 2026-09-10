package aprimorar.pessoas.aluno.api;

import java.util.UUID;

public record Aluno(UUID id, String nome, String escola, boolean ativo) {
}
