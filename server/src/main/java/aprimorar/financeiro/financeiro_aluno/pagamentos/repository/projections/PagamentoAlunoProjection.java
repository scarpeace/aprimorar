package aprimorar.financeiro.financeiro_aluno.pagamentos.repository.projections;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface PagamentoAlunoProjection {

    UUID getId();

    UUID getAlunoId();

    LocalDate getDataPagamento();

    FormaPagamentoEnum getFormaPagamento();

    String getComprovanteUrl();

    BigDecimal getValorTotal();

    Long getQuantidadeCobrancas();
}
