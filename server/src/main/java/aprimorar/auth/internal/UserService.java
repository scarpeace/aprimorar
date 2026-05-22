package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDTO;
import aprimorar.auth.api.dto.UserResponseDTO;
import aprimorar.auth.api.exception.AuthErrorCode;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.DomainBusinessException;
import aprimorar.shared.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            throw new DomainBusinessException(AuthErrorCode.INVALID_USERNAME.getStatus(), AuthErrorCode.INVALID_USERNAME.getMessage());
        }

        var userFromDb = userRepository.findByUsername(normalizedUsername);

        if (userFromDb.isPresent()) {
            throw new DomainBusinessException(AuthErrorCode.USERNAME_TAKEN.getStatus(), AuthErrorCode.USERNAME_TAKEN.getMessage());
        }

        if (!isRoleAllowedForAuthUser(dto.role())) {
            throw new DomainBusinessException(AuthErrorCode.INVALID_ROLE.getStatus(), AuthErrorCode.INVALID_ROLE.getMessage());
        }

        if (dto.role() == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
            throw new DomainBusinessException(AuthErrorCode.ADMIN_ALREADY_EXISTS.getStatus(), AuthErrorCode.ADMIN_ALREADY_EXISTS.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        var user = userMapper.toEntity(dto, normalizedUsername, encodedPassword);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DomainBusinessException(AuthErrorCode.USER_NOT_FOUND.getStatus(), AuthErrorCode.USER_NOT_FOUND.getMessage()));

        if (user.getRole() == Role.ADMIN) {
            throw new DomainBusinessException(AuthErrorCode.ADMIN_CANNOT_BE_CHANGED.getStatus(), AuthErrorCode.ADMIN_CANNOT_BE_CHANGED.getMessage());
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DomainBusinessException(AuthErrorCode.USER_NOT_FOUND.getStatus(), AuthErrorCode.USER_NOT_FOUND.getMessage()));

        if (user.getRole() == Role.ADMIN) {
            throw new DomainBusinessException(AuthErrorCode.ADMIN_CANNOT_BE_CHANGED.getStatus(), AuthErrorCode.ADMIN_CANNOT_BE_CHANGED.getMessage());
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
