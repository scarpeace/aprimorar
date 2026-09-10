package aprimorar.auth.autenticacao.service;

import aprimorar.auth.autenticacao.web.dto.AuthResponseDTO;
import aprimorar.auth.usuario.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO authenticate(String email, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(email, rawPassword)
        );
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        return AuthResponseDTO.toDto(accessToken, jwtService.expiresInSeconds(), user);
    }
}
