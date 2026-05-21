package aprimorar.auth.internal;

import aprimorar.auth.api.dto.CreateUserDto;
import aprimorar.shared.MapperUtils;

import java.util.Optional;

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
    public void createUser(CreateUserDto dto) {

        var userFromDb = findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User(dto.username(), passwordEncoder.encode(dto.password()), dto.role(), true);
        userRepository.save(user);
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

    @Transactional
    public void toggleActive(String username) {
        var user = findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.toggleActive();
    }
}
