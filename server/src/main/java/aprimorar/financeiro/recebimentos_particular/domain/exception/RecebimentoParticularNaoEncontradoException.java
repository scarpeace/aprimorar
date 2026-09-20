package aprimorar.financeiro.recebimentos_particular.domain.exception;

public class RecebimentoParticularNaoEncontradoException extends RuntimeException {

    public RecebimentoParticularNaoEncontradoException() {
        super("Recebimento não encontrado");
    }
}
