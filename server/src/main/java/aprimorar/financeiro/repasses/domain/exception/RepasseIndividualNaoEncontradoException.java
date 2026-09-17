package aprimorar.financeiro.repasses.domain.exception;

public class RepasseIndividualNaoEncontradoException extends RuntimeException {

    public RepasseIndividualNaoEncontradoException() {
        super("Repasse individual não encontrado");
    }
}
