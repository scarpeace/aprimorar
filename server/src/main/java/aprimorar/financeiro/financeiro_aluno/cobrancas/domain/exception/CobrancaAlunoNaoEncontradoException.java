package aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception;

public class CobrancaAlunoNaoEncontradoException extends RuntimeException {

    public CobrancaAlunoNaoEncontradoException() {
        super("Cobrança de aluno não encontrada no banco de dados");
    }
}
