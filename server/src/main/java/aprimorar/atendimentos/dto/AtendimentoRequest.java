package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Formato de payload para cadastro e atualização de atendimento")
public record AtendimentoRequest(

    @Schema(nullable = false, description = "Descrição do atendimento", example = "Sessão focada em revisão de matemática básica")
    String descricao,

    @NotNull(message = "Informe o tipo do atendimento")
    @Schema(nullable = false, description = "Tipo de conteúdo do atendimento", example = "MENTORIA")
    TipoAtendimento tipo,

    @NotNull(message = "Informe a data de início")
    @Future(message = "A data de início precisa ser no futuro")
    @Schema(nullable = false, description = "Data e hora de início do atendimento", example = "2026-11-20T14:00:00Z")
    LocalDateTime inicio,

    @NotNull(message = "Informe a duração")
    @Positive(message = "A duração precisa ser maior que zero")
    @Schema(nullable = false, description = "Duração do atendimento em horas", example = "1.5")
    Double duracao,

    @NotNull(message = "Informe o valor")
    @PositiveOrZero(message = "O valor precisa ser maior ou igual a zero")
    @Schema(nullable = false, description = "Valor do atendimento cobrado do aluno", example = "150.00")
    BigDecimal valor,

    @NotNull(message = "Informe o repasse")
    @PositiveOrZero(message = "O repasse precisa ser maior ou igual a zero")
    @Schema(nullable = false, description = "Valor de repasse do atendimento ao colaborador", example = "100.00")
    BigDecimal repasse,

    @NotNull(message = "Informe o aluno")
    @Schema(nullable = false, description = "ID do aluno vinculado ao atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,

    @NotNull(message = "Informe o colaborador")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID colaboradorId
) {
    public Atendimento toEntity(String nomeAluno, String nomeColaborador) {
        return new Atendimento(
            descricao,
            inicio,
            duracao,
            repasse,
            valor,
            tipo,
            alunoId,
            nomeAluno,
            colaboradorId,
            nomeColaborador
        );
    }
}
