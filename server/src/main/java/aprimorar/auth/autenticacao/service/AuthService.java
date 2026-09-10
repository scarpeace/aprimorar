package aprimorar.auth.autenticacao.service;

import aprimorar.auth.autenticacao.web.dto.AuthResponseDTO;
import aprimorar.auth.usuario.service.UserService;
import aprimorar.auth.usuario.domain.User;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(String email, String rawPassword) {
        User user = userService
            .findActiveByUsername(email)
            .orElseThrow(() -> new BadCredentialsException("Email ou senha invalidos"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Email ou senha invalidos");
        }

        String accessToken = jwtService.generateToken(user);
        return AuthResponseDTO.toDto(accessToken, jwtService.expiresInSeconds(), user);
    }
}
