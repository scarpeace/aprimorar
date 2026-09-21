package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api;

public class RepasseParticularNaoEncontradoException extends RuntimeException {

    public RepasseParticularNaoEncontradoException() {
        super("Repasse particular não encontrado");
    }
}
