package aprimorar.auth.application;

import aprimorar.auth.web.dto.AuthResponseDTO;
import aprimorar.auth.web.dto.UserResponseDTO;
import aprimorar.auth.domain.User;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final long ACCESS_TOKEN_EXPIRES_IN_SECONDS = 8 * 60 * 60;
    private static final String ISSUER = "aprimorar-api";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(String email, String rawPassword) {
        User user = userService
            .findActiveByUsername(email)
            .orElseThrow(() -> new BadCredentialsException("Email ou senha invalidos"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Email ou senha invalidos");
        }

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ACCESS_TOKEN_EXPIRES_IN_SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(expiresAt)
            .claim("username", user.getUsername())
            .claim("role", user.getRole().name())
            .claim("scope", user.getRole().name())
            .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new AuthResponseDTO(accessToken, ACCESS_TOKEN_EXPIRES_IN_SECONDS, user.getUsername(), user.getRole());
    }


}
