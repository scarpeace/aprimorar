package aprimorar.financeiro.recebimentos_alunos.domain.exception;

public class CobrancaNaoEncontradaException extends RuntimeException {

    public CobrancaNaoEncontradaException() {
        super("Cobrança não encontrada");
    }
}
