package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;

public record TransacaoResponseDTO(
        UUID id,
        UUID pagadorId,
        UUID recebedorId,
        Double valor,
        LocalDateTime dataEfetivada,
        TipoTransacao tipo,
        FormaPagamento formaPagamento,
        StatusTransacao status,
        CategoriaTransacao categoria,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
