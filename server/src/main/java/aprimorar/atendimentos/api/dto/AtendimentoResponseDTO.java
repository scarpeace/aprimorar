package aprimorar.atendimentos.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import aprimorar.atendimentos.internal.TipoAtendimentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do atendimento retornados pela API")
public record AtendimentoResponseDTO(
    @NotNull
    @Schema(nullable = false, description = "Identificador unico do atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Nullable
    @Schema(nullable = false, description = "Descricao do atendimento", example = "Sessao focada em revisao de matematica basica")
    String description,

    @NotNull
    @Schema(nullable = false, description = "Conteudo do atendimento (aula, mentoria etc.)", example = "MENTORIA")
    TipoAtendimentoEnum content,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de inicio do atendimento", example = "2023-11-20T14:00:00Z")
    Instant startDate,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de fim do atendimento", example = "2023-11-20T16:00:00Z")
    Instant endDate,

    @NotNull
    @Schema(nullable = false, description = "Duracao do atendimento em horas", example = "1.5")
    Double duration,

    @NotNull
    @Schema(nullable = false, description = "Valor do atendimento cobrado do aluno", example = "150.00")
    BigDecimal price,

    @NotNull
    @Schema(nullable = false, description = "Pagamento do atendimento ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull
    @Schema(nullable = false, description = "Lucro do atendimento", example = "50.00")
    BigDecimal profit,

    @NotNull
    @Schema(nullable = false, description = "ID do estudante vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID studentId,

    @NotNull
    @Schema(nullable = false, description = "Nome do aluno vinculado ao atendimento", example = "John Doe")
    String studentName,

    @NotNull
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID employeeId,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado ao atendimento", example = "Jane Doe")
    String employeeName,

    @Nullable
    @Schema(nullable = true, description = "Data de pagamento ao colaborador", example = "2024-03-10T15:33:42Z")
    Instant employeePaymentDate,

    @Nullable
    @Schema(nullable = true, description = "Data de cobrança do aluno", example = "2024-03-10T15:33:42Z")
    Instant studentChargeDate,

    @NotNull
    @Schema(nullable = false, description = "Data de criacao do atendimento", example = "2024-03-10T15:33:42Z")
    Instant createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualizacao do atendimento", example = "2024-03-10T15:33:42Z")
    Instant updatedAt
) {}
