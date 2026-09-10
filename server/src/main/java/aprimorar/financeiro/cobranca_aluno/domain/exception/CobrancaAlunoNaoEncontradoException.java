package aprimorar.financeiro.cobranca_aluno.domain.exception;

public class CobrancaAlunoNaoEncontradoException extends RuntimeException {

    public CobrancaAlunoNaoEncontradoException() {
        super("Pagamento do aluno não encontrado no banco de dados");
    }
}
