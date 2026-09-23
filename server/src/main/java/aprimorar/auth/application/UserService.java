package aprimorar.auth.application;

import aprimorar.auth.domain.User;
import aprimorar.auth.domain.exception.UserAlreadyExistsException;
import aprimorar.auth.domain.exception.UserNotFoundException;
import aprimorar.auth.infrastructure.UserRepository;
import aprimorar.auth.web.dto.UserCreateRequest;
import aprimorar.auth.web.dto.UserResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String email = request.email();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Já existe um usuário com este e-mail.");
        }

        User user = new User(
            email,
            passwordEncoder.encode(request.password()),
            request.role()
        );

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
            .stream()
            .map(UserResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID userId) {
        return UserResponse.from(findUser(userId));
    }

    @Transactional
    public void deactivate(UUID userId) {
        User user = findUser(userId);
        user.deactivate();
        refreshTokenService.revokeAll(user.getId());
    }

    @Transactional
    public void activate(UUID userId) {
        findUser(userId).activate();
    }

    @Transactional
    public void delete(UUID userId) {
        User user = findUser(userId);
        refreshTokenService.deleteAll(user.getId());
        userRepository.delete(user);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }
}
