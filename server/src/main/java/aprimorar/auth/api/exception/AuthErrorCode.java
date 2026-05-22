package aprimorar.auth.api.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode {
    USERNAME_TAKEN(HttpStatus.CONFLICT, "Nome de usuário já utilizado"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "Perfil de acesso inválido"),
    ADMIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "Já existe um usuário ADMIN"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Usuário não encontrado"),
    ADMIN_CANNOT_BE_CHANGED(HttpStatus.CONFLICT, "Não é permitido alterar o usuário ADMIN"),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "E-mail inválido");

    private final HttpStatus status;
    private final String message;

    AuthErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
