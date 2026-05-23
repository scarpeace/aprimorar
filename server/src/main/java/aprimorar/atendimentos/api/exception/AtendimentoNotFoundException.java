package aprimorar.atendimentos.api.exception;

public class AtendimentoNotFoundException extends RuntimeException {
    public AtendimentoNotFoundException(){
        super("Evento não encontrado no banco de dados");
    }
}

