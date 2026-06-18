package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;

public record TransacaoFiltroRequest(
    UUID pagadorId,
    UUID recebedorId,
    LocalDateTime dataEfetivada,
    TipoTransacao tipo,
    CategoriaTransacao categoria,
    StatusTransacao status,
    FormaPagamento formaPagamento,
    String busca
) {
}
