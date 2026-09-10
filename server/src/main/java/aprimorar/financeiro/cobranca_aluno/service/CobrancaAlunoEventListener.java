package aprimorar.financeiro.cobranca_aluno.service;

import aprimorar.atendimentos.individuais.api.AtendimentoCreatedEvent;
import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import aprimorar.financeiro.cobranca_aluno.repository.CobrancaAlunoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CobrancaAlunoEventListener {

    private static final Logger log = LoggerFactory.getLogger(CobrancaAlunoEventListener.class);

    private final CobrancaAlunoRepository cobrancaRepository;

    public CobrancaAlunoEventListener(CobrancaAlunoRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void criarCobrancaPendente(AtendimentoCreatedEvent event) {
        CobrancaAlunoEntity cobranca = new CobrancaAlunoEntity(
            event.atendimentoId(),
            event.alunoId(),
            event.valor()
        );

        cobrancaRepository.save(cobranca);
        log.info("Cobrança pendente criada para o atendimento {}.", event.atendimentoId());
    }
}
