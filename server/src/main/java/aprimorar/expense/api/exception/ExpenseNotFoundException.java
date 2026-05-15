package aprimorar.expense.api.exception;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException() {
        super("Despesa geral não encontrada");
    }
}
