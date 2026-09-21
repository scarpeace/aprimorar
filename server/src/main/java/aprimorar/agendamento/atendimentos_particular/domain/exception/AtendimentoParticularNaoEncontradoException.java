package aprimorar.agendamento.atendimentos_particular.domain.exception;

public class AtendimentoParticularNaoEncontradoException extends RuntimeException {

    public AtendimentoParticularNaoEncontradoException() {
        super("Atendimento não encontrado");
    }
}
