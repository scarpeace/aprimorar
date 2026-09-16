package aprimorar.atendimentos.individuais.repository.repasse;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface RepasseLoteProjection {

    UUID getLoteId();

    UUID getColaboradorId();

    LocalDateTime getDataRepasse();

    FormaPagamento getFormaPagamento();

    String getComprovanteUrl();

    BigDecimal getValorTotal();

    Long getQuantidadeRepasses();
}
