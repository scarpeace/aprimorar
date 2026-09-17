package aprimorar.financeiro.cobrancas.repository.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.financeiro.common.FormaPagamentoEnum;

public interface CobrancaLoteProjection {

    UUID getLoteId();

    UUID getAlunoId();

    LocalDateTime getDataPagamento();

    FormaPagamentoEnum getFormaPagamento();

    String getComprovanteUrl();

    BigDecimal getValorTotal();

    Long getQuantidadeCobrancas();
}
