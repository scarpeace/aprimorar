package aprimorar.atendimentos.individuais.domain.exception;

public class AtendimentoNaoEncontradoException extends RuntimeException {

    public AtendimentoNaoEncontradoException() {
        super("Atendimento não encontrado");
    }
}
