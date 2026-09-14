package aprimorar.auth.refresh;

import aprimorar.auth.exception.AuthException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final SecureRandom random = new SecureRandom();
    private final long refreshTokenDays;

    public RefreshTokenService(
        RefreshTokenRepository repository,
        @Value("${app.auth.refresh-token-days}") long refreshTokenDays
    ) {
        this.repository = repository;
        this.refreshTokenDays = refreshTokenDays;
    }

    @Transactional
    public String create(UUID userId) {
        String rawToken = generateRandomToken();

        RefreshToken refreshToken = new RefreshToken(
            UUID.randomUUID(),
            userId,
            hash(rawToken),
            Instant.now().plus(refreshTokenDays, ChronoUnit.DAYS)
        );

        repository.save(refreshToken);

        return rawToken;
    }

    @Transactional(readOnly = true)
    public UUID validate(String rawToken) {
        RefreshToken token = repository
            .findByTokenHash(hash(rawToken))
            .orElseThrow(() -> new AuthException("Invalid refresh token"));

        if (!token.isValid()) {
            throw new AuthException("Invalid refresh token");
        }

        return token.getUserId();
    }

    @Transactional
    public void revoke(String rawToken) {
        repository.findByTokenHash(hash(rawToken)).ifPresent(RefreshToken::revoke);
    }

    private String generateRandomToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
