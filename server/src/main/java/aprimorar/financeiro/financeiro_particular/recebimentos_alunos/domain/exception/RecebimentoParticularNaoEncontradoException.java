package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.exception;

public class RecebimentoParticularNaoEncontradoException extends RuntimeException {

    public RecebimentoParticularNaoEncontradoException() {
        super("Recebimento não encontrado");
    }
}
