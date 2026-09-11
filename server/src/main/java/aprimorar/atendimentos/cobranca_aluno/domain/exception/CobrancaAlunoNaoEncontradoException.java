package aprimorar.atendimentos.cobranca_aluno.domain.exception;

public class CobrancaAlunoNaoEncontradoException extends RuntimeException {

    public CobrancaAlunoNaoEncontradoException() {
        super("Cobrança do aluno não encontrada no banco de dados");
    }
}
