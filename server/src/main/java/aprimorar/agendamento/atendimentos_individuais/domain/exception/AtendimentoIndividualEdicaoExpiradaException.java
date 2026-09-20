package aprimorar.agendamento.atendimentos_individuais.domain.exception;

public class AtendimentoIndividualEdicaoExpiradaException extends RuntimeException {

    public AtendimentoIndividualEdicaoExpiradaException() {
        super("A janela de 20 dias para editar as informações do atendimento encerrou");
    }
}
