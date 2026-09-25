package aprimorar.financeiro.repasses_colaboradores.config;

import aprimorar.financeiro.repasses_colaboradores.application.PagamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class RepasseStatusJob {

    private static final Logger log = LoggerFactory.getLogger(RepasseStatusJob.class);

    private final PagamentoService pagamentoService;

    RepasseStatusJob(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @Scheduled(
        cron = "${financeiro.jobs.status-cron:0 0 2 * * *}",
        zone = "America/Sao_Paulo"
    )
    void marcarAtrasados() {
        int atualizados = pagamentoService.marcarRepassesAtrasados();
        log.info("{} repasses marcados como atrasados", atualizados);
    }
}
