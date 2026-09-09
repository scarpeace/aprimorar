package aprimorar.despesas.domain.exception;

public class DespesaNaoEncontradaException extends RuntimeException {

    public DespesaNaoEncontradaException() {
        super("Despesa não encontrada no banco de dados");
    }
}
