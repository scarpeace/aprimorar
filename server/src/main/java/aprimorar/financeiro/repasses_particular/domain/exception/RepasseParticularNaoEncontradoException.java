package aprimorar.financeiro.repasses_particular.domain.exception;

public class RepasseParticularNaoEncontradoException extends RuntimeException {

    public RepasseParticularNaoEncontradoException() {
        super("Repasse particular não encontrado");
    }
}
