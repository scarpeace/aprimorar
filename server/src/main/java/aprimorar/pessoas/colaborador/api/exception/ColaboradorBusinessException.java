package aprimorar.pessoas.colaborador.api.exception;

import org.springframework.http.HttpStatus;

public class ColaboradorBusinessException extends RuntimeException {

    private final HttpStatus status;

    public ColaboradorBusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
