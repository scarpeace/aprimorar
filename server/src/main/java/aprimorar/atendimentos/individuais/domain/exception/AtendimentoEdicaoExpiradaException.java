package aprimorar.atendimentos.individuais.domain.exception;

public class AtendimentoEdicaoExpiradaException extends RuntimeException {

    public AtendimentoEdicaoExpiradaException() {
        super("A janela de 20 dias para editar as informações do atendimento encerrou");
    }
}
