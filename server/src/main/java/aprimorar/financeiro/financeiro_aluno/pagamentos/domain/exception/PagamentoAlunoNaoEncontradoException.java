package aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception;

public class PagamentoAlunoNaoEncontradoException extends RuntimeException {

    public PagamentoAlunoNaoEncontradoException() {
        super("Pagamento de aluno não encontrado");
    }
}
