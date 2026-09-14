package aprimorar.auth;

import aprimorar.auth.dto.LoginRequest;
import aprimorar.auth.dto.LoginResult;
import aprimorar.auth.exception.AuthException;
import aprimorar.auth.jwt.JwtService;
import aprimorar.auth.refresh.RefreshTokenService;
import aprimorar.auth.user.User;
import aprimorar.auth.user.UserRepository;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        JwtService jwtService,
        RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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
}
