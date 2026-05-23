package aprimorar.financeiro.api;

import java.math.BigDecimal;
import java.time.Instant;

public interface FinanceiroService {

    BigDecimal sumExpenses(Instant startDate, Instant endDate);
}
