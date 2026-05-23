package aprimorar.expense.api;

import java.math.BigDecimal;
import java.time.Instant;

public interface ExpenseService {

    BigDecimal sumExpenses(Instant startDate, Instant endDate);
}
