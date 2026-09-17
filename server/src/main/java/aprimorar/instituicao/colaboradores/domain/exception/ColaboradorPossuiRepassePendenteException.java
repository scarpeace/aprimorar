package aprimorar.instituicao.colaboradores.domain.exception;

public class ColaboradorPossuiRepassePendenteException extends RuntimeException {

    public ColaboradorPossuiRepassePendenteException() {
        super("Não é possível desativar um colaborador com repasse pendente");
    }
}
