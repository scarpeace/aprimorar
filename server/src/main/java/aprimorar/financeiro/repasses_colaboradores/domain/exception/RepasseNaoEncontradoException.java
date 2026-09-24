package aprimorar.financeiro.repasses_colaboradores.domain.exception;

public class RepasseNaoEncontradoException extends RuntimeException {

    public RepasseNaoEncontradoException() {
        super("Repasse não encontrado");
    }
}
