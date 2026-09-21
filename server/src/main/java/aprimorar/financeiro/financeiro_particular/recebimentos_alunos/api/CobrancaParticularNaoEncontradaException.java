package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api;

public class CobrancaParticularNaoEncontradaException extends RuntimeException {

    public CobrancaParticularNaoEncontradaException() {
        super("Cobrança não encontrada");
    }
}
