package aprimorar.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import aprimorar.auth.domain.UserEntity;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_EXPIRES_IN_SECONDS = 8 * 60 * 60;
    private static final String ISSUER = "aprimorar-api";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("Configuração jwt.secret ausente");
        }
        if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("Configuração jwt.secret deve ter ao menos 32 bytes");
        }

        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.verifier = JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build();
    }

    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ACCESS_TOKEN_EXPIRES_IN_SECONDS);

        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(user.getId().toString())
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expiresAt))
            .sign(algorithm);
    }

    public long expiresInSeconds() {
        return ACCESS_TOKEN_EXPIRES_IN_SECONDS;
    }

    public Optional<UUID> validateAndExtractUserId(String token) {
        try {
            String subject = verifier.verify(token).getSubject();
            return Optional.of(UUID.fromString(subject));
        } catch (JWTVerificationException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

}
