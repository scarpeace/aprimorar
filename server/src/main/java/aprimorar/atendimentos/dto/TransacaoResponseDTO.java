package aprimorar.atendimentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.domain.Transacao;
import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados da transação retornados pela API")
public record TransacaoResponseDTO(
        @NotNull
        @Schema(description = "Identificador da transação", example = "1")
        Long id,

        @NotNull
        @Schema(description = "ID do pagador", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID pagadorId,

        @NotNull
        @Schema(description = "Nome do pagador", example = "Aprimorar")
        String nomePagador,

        @NotNull
        @Schema(description = "ID do recebedor", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID recebedorId,

        @NotNull
        @Schema(description = "Nome do recebedor", example = "João Pereira")
        String nomeRecebedor,

        @NotNull
        @Schema(description = "Valor da transação", example = "150.00")
        BigDecimal valor,

        @Schema(description = "Data em que a transação foi efetivada", example = "2024-03-10T15:33:42", nullable = true)
        LocalDateTime dataEfetivada,

        @NotNull
        @Schema(description = "Tipo da transação", example = "SAIDA")
        TipoTransacao tipo,

        @Schema(description = "Forma de pagamento usada na efetivação", example = "PIX", nullable = true)
        FormaPagamento formaPagamento,

        @NotNull
        @Schema(description = "Status atual da transação", example = "PENDENTE")
        StatusTransacao status,

        @NotNull
        @Schema(description = "Categoria financeira", example = "DESPENSA")
        CategoriaTransacao categoria,

        @NotNull
        @Schema(description = "Data de criação", example = "2024-03-10T15:33:42")
        LocalDateTime createdAt
) {

    public static TransacaoResponseDTO toDto(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getPagadorId(),
                transacao.getNomePagador(),
                transacao.getRecebedorId(),
                transacao.getNomeRecebedor(),
                transacao.getValor(),
                transacao.getDataEfetivada(),
                transacao.getTipo(),
                transacao.getFormaPagamento(),
                transacao.getStatus(),
                transacao.getCategoria(),
                transacao.getCreatedAt()
        );
    }
}
