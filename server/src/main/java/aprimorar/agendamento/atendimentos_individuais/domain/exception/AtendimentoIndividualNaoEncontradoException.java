package aprimorar.agendamento.atendimentos_individuais.domain.exception;

public class AtendimentoIndividualNaoEncontradoException extends RuntimeException {

    public AtendimentoIndividualNaoEncontradoException() {
        super("Atendimento não encontrado");
    }
}
