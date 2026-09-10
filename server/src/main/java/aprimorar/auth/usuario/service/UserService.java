package aprimorar.auth.usuario.service;

import aprimorar.auth.usuario.web.dto.UserRequestDTO;
import aprimorar.auth.usuario.web.dto.UserResponseDTO;
import aprimorar.auth.Role;
import aprimorar.auth.usuario.domain.User;
import aprimorar.auth.usuario.domain.exception.UsuarioDadosInvalidosException;
import aprimorar.auth.usuario.domain.exception.UsuarioDuplicadoException;
import aprimorar.auth.usuario.domain.exception.UsuarioEstadoInvalidoException;
import aprimorar.auth.usuario.domain.exception.UsuarioNaoEncontradoException;
import aprimorar.auth.usuario.repository.UserRepository;
import aprimorar.common.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final String adminUsername;
    private final String adminPassword;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserMapper userMapper,
        @Value("${aprimorar.admin-username:}") String adminUsername,
        @Value("${aprimorar.admin-password:}") String adminPassword
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
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

        String encodedPassword = passwordEncoder.encode(dto.password());
        var user = userMapper.toEntity(dto, normalizedUsername, encodedPassword);
        userRepository.save(user);

        return userMapper.toDto(user);
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

        userRepository.findByUsername(normalizedUsername).ifPresentOrElse(user -> {
            user.promoteToAdmin(encodedPassword);
        }, () -> userRepository.save(new User(
            normalizedUsername,
            encodedPassword,
            Role.ADMIN,
            true
        )));
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new UsuarioEstadoInvalidoException("Não é permitido alterar este usuário");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new UsuarioEstadoInvalidoException("Não é permitido alterar este usuário");
        }

        user.toggleActive();
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);

        return userRepository.findByUsername(normalizedUsername)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<User> findActiveByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        if (normalizedUsername == null) {
            return Optional.empty();
        }

        return userRepository.findByUsernameAndActiveTrue(normalizedUsername);
    }
}
