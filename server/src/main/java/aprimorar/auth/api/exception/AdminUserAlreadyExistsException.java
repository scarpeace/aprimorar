package aprimorar.auth.api.exception;

public class AdminUserAlreadyExistsException extends RuntimeException {

    public AdminUserAlreadyExistsException() {
        super("Ja existe um usuario ADMIN");
    }
}
