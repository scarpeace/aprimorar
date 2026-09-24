package aprimorar.financeiro.repasses_colaboradores.domain.exception;

public class RepasseJaExistenteException extends RuntimeException {

    public RepasseJaExistenteException() {
        super("Já existe um repasse para o atendimento informado.");
    }
}
