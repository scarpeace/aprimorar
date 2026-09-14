package aprimorar.auth;

import aprimorar.auth.dto.AuthMeResponse;
import aprimorar.auth.dto.LoginRequest;
import aprimorar.auth.dto.LoginResult;
import aprimorar.auth.exception.AuthException;
import aprimorar.auth.jwt.JwtService;
import aprimorar.auth.refresh.RefreshTokenService;
import aprimorar.auth.user.Role;
import aprimorar.auth.user.User;
import aprimorar.auth.user.UserRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        JwtService jwtService,
        RefreshTokenService refreshTokenService,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public void ensureAdmin(String email, String password) {
        if (email.isBlank() || password.isBlank() || userRepository.findByEmail(email).isPresent()) {
            return;
        }

        userRepository.save(new User(email, passwordEncoder.encode(password), Role.ADMIN));
        log.info("---- ADMIN OK ----");
    }

    public LoginResult login(LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            throw new AuthException("Invalid credentials");
        }

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.create(user.getId());

        return new LoginResult(accessToken, refreshToken);
    }

    public String refresh(String refreshToken) {
        UUID userId = refreshTokenService.validate(refreshToken);

        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new AuthException("User not found"));

        return jwtService.generateAccessToken(user);
    }

    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    public AuthMeResponse me(UUID userId) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new AuthException("User not found"));

        return new AuthMeResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
