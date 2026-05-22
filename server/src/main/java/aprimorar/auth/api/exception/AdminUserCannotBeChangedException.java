package aprimorar.auth.api.exception;

public class AdminUserCannotBeChangedException extends RuntimeException {

    public AdminUserCannotBeChangedException() {
        super("Nao e permitido alterar o usuario ADMIN");
    }
}
