package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDTO;
import aprimorar.auth.api.dto.UserResponseDTO;
import aprimorar.auth.api.exception.AdminUserAlreadyExistsException;
import aprimorar.auth.api.exception.AdminUserCannotBeChangedException;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        var userFromDb = findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username já utilizado");
        }

        if(dto.role() == Role.STUDENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possíve criar um usuário com role STUDENT");
        }

        if(dto.role() == Role.PARENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possíve criar um usuário com role PARENT");
        }

        if (dto.role() == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
            throw new AdminUserAlreadyExistsException();
        }


        var user = new User(dto.username(), passwordEncoder.encode(dto.password()), dto.role(), true);
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getRole() == Role.ADMIN) {
            throw new AdminUserCannotBeChangedException();
        }

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getRole() == Role.ADMIN) {
            throw new AdminUserCannotBeChangedException();
        }

        user.toggleActive();
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
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

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
