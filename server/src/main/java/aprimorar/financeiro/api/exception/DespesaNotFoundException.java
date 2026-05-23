package aprimorar.financeiro.api.exception;

public class DespesaNotFoundException extends RuntimeException {
    public DespesaNotFoundException() {
        super("Despesa geral não encontrada");
    }
}
