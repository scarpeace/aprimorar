package aprimorar.financeiro.pagamento_aluno.service;

import aprimorar.atendimentos.individuais.api.AtendimentoCreatedEvent;
import aprimorar.financeiro.pagamento_aluno.domain.PagamentoAlunoEntity;
import aprimorar.financeiro.pagamento_aluno.repository.PagamentoAlunoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PagamentoAlunoEventListener {

    private static final Logger log = LoggerFactory.getLogger(PagamentoAlunoEventListener.class);

    private final PagamentoAlunoRepository pagamentoRepository;

    public PagamentoAlunoEventListener(PagamentoAlunoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void criarPagamentoPendente(AtendimentoCreatedEvent event) {
        PagamentoAlunoEntity pagamento = new PagamentoAlunoEntity(
            event.atendimentoId(),
            event.alunoId(),
            event.valor()
        );

        pagamentoRepository.save(pagamento);
        log.info("Pagamento pendente criado para o atendimento {}.", event.atendimentoId());
    }
}
