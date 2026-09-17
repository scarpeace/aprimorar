package aprimorar.atendimentos.individuais.atendimentos.domain.exception;

public class AtendimentoIndividualNaoEncontradoException extends RuntimeException {

    public AtendimentoIndividualNaoEncontradoException() {
        super("Atendimento não encontrado");
    }
}
