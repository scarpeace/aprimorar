package aprimorar.agendamento.atendimentos_particular.domain.exception;

public class AtendimentoParticularEdicaoExpiradaException extends RuntimeException {

    public AtendimentoParticularEdicaoExpiradaException() {
        super("A janela de 20 dias para editar as informações do atendimento encerrou");
    }
}
