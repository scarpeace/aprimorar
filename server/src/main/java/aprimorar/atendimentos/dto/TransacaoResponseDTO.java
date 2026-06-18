package aprimorar.atendimentos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.domain.Transacao;
import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;

public record TransacaoResponseDTO(
        Long id,
        UUID pagadorId,
        UUID recebedorId,
        BigDecimal valor,
        LocalDateTime dataEfetivada,
        TipoTransacao tipo,
        FormaPagamento formaPagamento,
        StatusTransacao status,
        CategoriaTransacao categoria,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TransacaoResponseDTO toDto(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getPagadorId(),
                transacao.getRecebedorId(),
                transacao.getValor(),
                transacao.getDataEfetivada(),
                transacao.getTipo(),
                transacao.getFormaPagamento(),
                transacao.getStatus(),
                transacao.getCategoria(),
                transacao.getCreatedAt(),
                transacao.getUpdatedAt()
        );
    }
}
