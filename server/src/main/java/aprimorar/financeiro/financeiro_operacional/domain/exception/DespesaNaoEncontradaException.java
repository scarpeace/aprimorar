package aprimorar.financeiro.financeiro_operacional.domain.exception;

public class DespesaNaoEncontradaException extends RuntimeException {

    public DespesaNaoEncontradaException() {
        super("Despesa não encontrada no banco de dados");
    }
}
