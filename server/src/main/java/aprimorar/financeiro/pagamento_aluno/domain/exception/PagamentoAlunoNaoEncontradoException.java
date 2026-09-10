package aprimorar.financeiro.pagamento_aluno.domain.exception;

public class PagamentoAlunoNaoEncontradoException extends RuntimeException {

    public PagamentoAlunoNaoEncontradoException() {
        super("Pagamento do aluno não encontrado no banco de dados");
    }
}
