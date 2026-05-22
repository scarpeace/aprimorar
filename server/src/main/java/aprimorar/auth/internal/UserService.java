package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDto;
import aprimorar.auth.api.dto.UserResponseDto;
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
    public UserResponseDto createUser(UserRequestDto dto) {
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


        var user = new User(dto.username(), passwordEncoder.encode(dto.password()), dto.role(), true);
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDto toggleActive(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.toggleActive();
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
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

    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
