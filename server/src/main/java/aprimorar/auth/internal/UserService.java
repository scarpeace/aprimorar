package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDTO;
import aprimorar.auth.api.dto.UserResponseDTO;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.DomainBusinessException;
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
            throw new DomainBusinessException(HttpStatus.BAD_REQUEST, "INVALID_USERNAME");
        }

        var userFromDb = userRepository.findByUsername(normalizedUsername);
        if (userFromDb.isPresent()) {
            throw new DomainBusinessException(HttpStatus.CONFLICT, "USERNAME_TAKEN");
        }

        if (!isRoleAllowedForAuthUser(dto.role())) {
            throw new DomainBusinessException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ROLE");
        }

        if (dto.role() == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
            throw new DomainBusinessException(HttpStatus.CONFLICT, "ADMIN_ALREADY_EXISTS");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        var user = userMapper.toEntity(dto, normalizedUsername, encodedPassword);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DomainBusinessException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        if (user.getRole() == Role.ADMIN) {
            throw new DomainBusinessException(HttpStatus.CONFLICT, "ADMIN_CANNOT_BE_CHANGED");
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DomainBusinessException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        if (user.getRole() == Role.ADMIN) {
            throw new DomainBusinessException(HttpStatus.CONFLICT, "ADMIN_CANNOT_BE_CHANGED");
        }

        user.toggleActive();
        userRepository.save(user);
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

    private boolean isRoleAllowedForAuthUser(Role role) {
        return role == Role.EMPLOYEE || role == Role.ADMIN;
    }
}
