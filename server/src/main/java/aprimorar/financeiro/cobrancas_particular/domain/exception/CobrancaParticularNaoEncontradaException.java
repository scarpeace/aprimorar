package aprimorar.financeiro.cobrancas_particular.domain.exception;

public class CobrancaParticularNaoEncontradaException extends RuntimeException {

    public CobrancaParticularNaoEncontradaException() {
        super("Cobrança não encontrada");
    }
}
