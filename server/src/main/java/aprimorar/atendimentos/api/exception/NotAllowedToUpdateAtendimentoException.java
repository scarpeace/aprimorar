package aprimorar.atendimentos.api.exception;

public class NotAllowedToUpdateAtendimentoException extends RuntimeException {
    public NotAllowedToUpdateAtendimentoException(String message) {
        super(message);
    }
}
