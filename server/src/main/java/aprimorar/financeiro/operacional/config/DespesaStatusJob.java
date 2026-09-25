package aprimorar.financeiro.operacional.config;

import aprimorar.financeiro.operacional.service.DespesaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class DespesaStatusJob {

    private static final Logger log = LoggerFactory.getLogger(DespesaStatusJob.class);

    private final DespesaService despesaService;

    DespesaStatusJob(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @Scheduled(
        cron = "${financeiro.jobs.status-cron:0 0 2 * * *}",
        zone = "America/Sao_Paulo"
    )
    void atualizarAtrasos() {
        int atualizadas = despesaService.atualizarStatusAtrasos();
        log.info("{} despesas tiveram o status de atraso atualizado", atualizadas);
    }
}
