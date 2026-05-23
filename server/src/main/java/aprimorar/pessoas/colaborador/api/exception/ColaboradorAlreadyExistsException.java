package aprimorar.pessoas.colaborador.api.exception;

public class ColaboradorAlreadyExistsException extends RuntimeException {
    public ColaboradorAlreadyExistsException(String message) {
        super(message);
    }
}
