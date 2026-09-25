package aprimorar.financeiro.recebimentos_alunos.config;

import aprimorar.financeiro.recebimentos_alunos.application.RecebimentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class CobrancaStatusJob {

    private static final Logger log = LoggerFactory.getLogger(CobrancaStatusJob.class);

    private final RecebimentoService recebimentoService;

    CobrancaStatusJob(RecebimentoService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @Scheduled(
        cron = "${financeiro.jobs.status-cron:0 0 2 * * *}",
        zone = "America/Sao_Paulo"
    )
    void marcarAtrasadas() {
        int atualizadas = recebimentoService.marcarCobrancasAtrasadas();
        log.info("{} cobranças marcadas como atrasadas", atualizadas);
    }
}
