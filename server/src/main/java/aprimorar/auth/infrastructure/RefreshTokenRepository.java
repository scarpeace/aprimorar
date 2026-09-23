package aprimorar.auth.infrastructure;

import aprimorar.auth.domain.RefreshToken;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

    @Modifying
    @Query("""
        DELETE FROM RefreshToken token
        WHERE token.revoked = true
           OR token.expiresAt <= :now
        """)
    int deleteInactive(@Param("now") Instant now);
}
