package aprimorar.auth.service;

import aprimorar.auth.web.dto.UserRequestDTO;
import aprimorar.auth.web.dto.UserResponseDTO;
import aprimorar.auth.Role;
import aprimorar.auth.domain.User;
import aprimorar.auth.infrastructure.persistence.UserRepository;
import aprimorar.auth.web.exception.UserBusinessException;
import aprimorar.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        String normalizedUsername = MapperUtils.normalizeEmail(dto.username());
        if (normalizedUsername == null) {
            throw new UserBusinessException(HttpStatus.BAD_REQUEST, "E-mail em formato inválido");
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        if (dto.role() != Role.COLABORADOR) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é possível criar um usuário com este perfil");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        var user = userMapper.toEntity(dto, normalizedUsername, encodedPassword);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public void ensureAdminUser(String username, String password) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        if (normalizedUsername == null) {
            throw new IllegalStateException("Configuração aprimorar.admin-username inválida");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Configuração aprimorar.admin-password ausente");
        }

        String encodedPassword = passwordEncoder.encode(password);

        userRepository.findByUsername(normalizedUsername).ifPresentOrElse(user -> {
            user.syncAdminAccess(encodedPassword);
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
                .orElseThrow(() -> new UserBusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.SISTEMA) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é permitido alterar este usuário");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserBusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.SISTEMA) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é permitido alterar este usuário");
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
                .orElseThrow(() -> new UserBusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
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
