package aprimorar.pessoas.aluno.api.exception;

public class AlunoAlreadyExistsException extends RuntimeException {
    public AlunoAlreadyExistsException(String message) {
        super(message);
    }
}
