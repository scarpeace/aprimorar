package aprimorar.financeiro.pagamentos_colaboradores.domain.exception;

public class RepasseNaoEncontradoException extends RuntimeException {

    public RepasseNaoEncontradoException() {
        super("Repasse particular não encontrado");
    }
}
