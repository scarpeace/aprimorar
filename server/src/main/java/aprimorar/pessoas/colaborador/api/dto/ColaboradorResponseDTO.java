package aprimorar.pessoas.colaborador.api.dto;

import aprimorar.pessoas.colaborador.internal.ColaboradorDutyEnum;
import aprimorar.pessoas.shared.address.dto.AddressResponseDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do colaborador retornados pela API")
public record ColaboradorResponseDTO(
        @NotNull
        @Schema(description = "Identificador único do colaborador", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @NotNull
        @Schema(description = "Nome completo do colaborador", example = "Joao Pereira")
        String name,
        @NotNull
        @Schema(description = "Data de nascimento do colaborador", example = "1990-05-21")
        LocalDate birthdate,
        @NotNull
        @Schema(description = "Chave PIX do colaborador", example = "joao@pix.com")
        String pix,
        @NotNull
        @Schema(description = "Telefone de contato do colaborador", example = "(61) 99999-9999")
        String contact,
        @NotNull
        @Schema(description = "CPF do colaborador", example = "123.456.789-00")
        String cpf,
        @NotNull
        @Schema(description = "Email do colaborador", example = "joao@empresa.com")
        String email,
        @NotNull
        @Schema(description = "Função do colaborador", example = "TEACHER")
        ColaboradorDutyEnum duty,
        @Schema(implementation = AddressResponseDTO.class, description = "Endereço do colaborador")
        @NotNull AddressResponseDTO address,
        @Schema(nullable = false, description = "Indica se o colaborador está ativo", example = "true")
        boolean active,
        @NotNull
        @Schema(description = "Data de criação do colaborador", example = "2024-03-10T15:33:42Z")
        Instant createdAt,
        @Schema(nullable = true, description = "Data da última atualização do colaborador", example = "2024-03-11T11:10:00Z")
        @Nullable
        Instant updatedAt
) {}
