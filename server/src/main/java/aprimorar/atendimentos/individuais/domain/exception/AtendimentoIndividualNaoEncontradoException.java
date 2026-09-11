package aprimorar.atendimentos.individuais.domain.exception;

public class AtendimentoIndividualNaoEncontradoException extends RuntimeException {

    public AtendimentoIndividualNaoEncontradoException() {
        super("Atendimento não encontrado");
    }
}
