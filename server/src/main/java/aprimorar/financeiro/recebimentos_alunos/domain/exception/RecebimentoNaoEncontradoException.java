package aprimorar.financeiro.recebimentos_alunos.domain.exception;

public class RecebimentoNaoEncontradoException extends RuntimeException {

    public RecebimentoNaoEncontradoException() {
        super("Recebimento não encontrado");
    }
}
