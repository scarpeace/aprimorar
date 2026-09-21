package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.exception;

public class PagamentoParticularNaoEncontradoException extends RuntimeException {

    public PagamentoParticularNaoEncontradoException() {
        super("Pagamento de repasse não encontrado");
    }
}
