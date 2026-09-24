package aprimorar.financeiro.recebimentos_alunos.domain.exception;

public class CobrancaJaExistenteException extends RuntimeException {

    public CobrancaJaExistenteException() {
        super("Já existe uma cobrança para o atendimento informado.");
    }
}
