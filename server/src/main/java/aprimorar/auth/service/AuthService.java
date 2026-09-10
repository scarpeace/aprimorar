package aprimorar.auth.service;

import aprimorar.auth.Role;
import aprimorar.auth.domain.UserEntity;
import aprimorar.auth.domain.exception.UsuarioDadosInvalidosException;
import aprimorar.auth.domain.exception.UsuarioDuplicadoException;
import aprimorar.auth.domain.exception.UsuarioEstadoInvalidoException;
import aprimorar.auth.domain.exception.UsuarioNaoEncontradoException;
import aprimorar.auth.repository.UserRepository;
import aprimorar.auth.web.dto.AuthResponseDTO;
import aprimorar.auth.web.dto.UserRequestDTO;
import aprimorar.auth.web.dto.UserResponseDTO;
import aprimorar.common.utils.MapperUtils;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AuthService(
        JwtService jwtService,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${aprimorar.admin-username:}") String adminUsername,
        @Value("${aprimorar.admin-password:}") String adminPassword
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(this);
        provider.setPasswordEncoder(passwordEncoder);
        this.authenticationManager = new ProviderManager(provider);
    }

    public AuthResponseDTO authenticate(String email, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(email, rawPassword)
        );
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        return AuthResponseDTO.toDto(accessToken, jwtService.expiresInSeconds(), user);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        String normalizedUsername = MapperUtils.normalizeEmail(dto.username());
        if (normalizedUsername == null) {
            throw new UsuarioDadosInvalidosException("E-mail em formato inválido");
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new UsuarioDuplicadoException("E-mail já cadastrado");
        }

        if (dto.role() != Role.COLABORADOR) {
            throw new UsuarioEstadoInvalidoException("Não é possível criar um usuário com este perfil");
        }

        UserEntity user = new UserEntity(
            normalizedUsername,
            passwordEncoder.encode(dto.password()),
            dto.role(),
            true
        );
        userRepository.save(user);
        return UserResponseDTO.toDto(user);
    }

    @Transactional
    public void ensureAdminUser() {
        String normalizedUsername = MapperUtils.normalizeEmail(adminUsername);
        if (normalizedUsername == null) {
            throw new IllegalStateException("Configuração aprimorar.admin-username inválida");
        }

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException("Configuração aprimorar.admin-password ausente");
        }

        String encodedPassword = passwordEncoder.encode(adminPassword);
        userRepository.findByUsername(normalizedUsername).ifPresentOrElse(
            user -> user.promoteToAdmin(encodedPassword),
            () -> userRepository.save(new UserEntity(normalizedUsername, encodedPassword, Role.ADMIN, true))
        );
    }

    @Transactional
    public void deleteUser(UUID id) {
        UserEntity user = findUserOrThrow(id);
        ensureUserCanBeChanged(user);
        userRepository.delete(user);
    }

    @Transactional
    public void deactivateUser(UUID id) {
        UserEntity user = findUserOrThrow(id);
        ensureUserCanBeChanged(user);
        user.deactivate();
    }

    @Transactional
    public void activateUser(UUID id) {
        UserEntity user = findUserOrThrow(id);
        ensureUserCanBeChanged(user);
        user.activate();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
            .map(UserResponseDTO::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findUserByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        return userRepository.findByUsername(normalizedUsername)
            .map(UserResponseDTO::toDto)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    @Override
    public UserEntity loadUserByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        if (normalizedUsername == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return userRepository.findByUsername(normalizedUsername)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private UserEntity findUserOrThrow(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    private void ensureUserCanBeChanged(UserEntity user) {
        if (user.getRole() == Role.ADMIN) {
            throw new UsuarioEstadoInvalidoException("Não é permitido alterar este usuário");
        }
    }
}
