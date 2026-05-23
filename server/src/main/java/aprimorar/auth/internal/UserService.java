package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDTO;
import aprimorar.auth.api.dto.UserResponseDTO;
import aprimorar.auth.api.exception.UserBusinessException;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.enums.Role;

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

        if (dto.role() == Role.PARENT || dto.role() == Role.STUDENT || dto.role() == Role.ADMIN) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é possível criar um usuário com este perfil");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        var user = userMapper.toEntity(dto, normalizedUsername, encodedPassword);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserBusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é permitido alterar o usuário ADMIN");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserBusinessException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (user.getRole() == Role.ADMIN) {
            throw new UserBusinessException(HttpStatus.CONFLICT, "Não é permitido alterar o usuário ADMIN");
        }

        user.toggleActive();
        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        if (normalizedUsername == null) {
            return Optional.empty();
        }

        return userRepository.findByUsername(normalizedUsername);
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
