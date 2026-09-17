package aprimorar.financeiro.repasses.repository.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.financeiro.common.FormaPagamentoEnum;


public interface RepasseLoteProjection {

    UUID getLoteId();
    UUID getColaboradorId();
    LocalDateTime getDataRepasse();
    FormaPagamentoEnum getFormaPagamento();
    String getComprovanteUrl();
    BigDecimal getValorTotal();
    Long getQuantidadeRepasses();
}
