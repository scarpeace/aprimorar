package aprimorar.atendimentos.individuais.domain.exception;

public class CobrancaIndividualNaoEncontradoException extends RuntimeException {

    public CobrancaIndividualNaoEncontradoException() {
        super("Cobrança individual não encontrada no banco de dados");
    }
}
