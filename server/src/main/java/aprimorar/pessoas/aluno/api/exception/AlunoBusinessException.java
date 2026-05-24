package aprimorar.pessoas.aluno.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlunoBusinessException extends RuntimeException {
    public AlunoBusinessException(HttpStatus status, String message) {
        super(message);
    }
}
