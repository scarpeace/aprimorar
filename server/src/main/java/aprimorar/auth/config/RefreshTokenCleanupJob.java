package aprimorar.auth.config;

import aprimorar.auth.application.RefreshTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
class RefreshTokenCleanupJob {
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenCleanupJob.class);

    private final RefreshTokenService refreshTokenService;

    RefreshTokenCleanupJob(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Scheduled(cron = "${app.auth.refresh-token-cleanup-cron:0 0 * * * *}")
    void clean() {
        int deleted = refreshTokenService.deleteInactive();
        log.info("---- {} inactive refresh tokens deleted", deleted);
    }
}
