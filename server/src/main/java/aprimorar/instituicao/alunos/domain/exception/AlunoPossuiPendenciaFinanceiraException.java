package aprimorar.instituicao.alunos.domain.exception;

public class AlunoPossuiPendenciaFinanceiraException extends RuntimeException {

    public AlunoPossuiPendenciaFinanceiraException() {
        super("Não é possível desativar um aluno com cobrança pendente");
    }
}
