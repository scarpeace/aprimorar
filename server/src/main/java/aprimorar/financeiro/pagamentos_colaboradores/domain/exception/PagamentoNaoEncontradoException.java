package aprimorar.financeiro.pagamentos_colaboradores.domain.exception;

public class PagamentoNaoEncontradoException extends RuntimeException {

    public PagamentoNaoEncontradoException() {
        super("Pagamento de repasse não encontrado");
    }
}
