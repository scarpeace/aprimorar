package aprimorar.financeiro.pagamentos_particular.domain.exception;

public class PagamentoParticularNaoEncontradoException extends RuntimeException {

    public PagamentoParticularNaoEncontradoException() {
        super("Pagamento de repasse não encontrado");
    }
}
