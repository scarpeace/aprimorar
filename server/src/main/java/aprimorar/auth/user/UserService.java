package aprimorar.auth.user;

import aprimorar.auth.dto.UserCreateRequest;
import aprimorar.auth.dto.UserListResponse;
import aprimorar.auth.dto.UserResponse;
import aprimorar.auth.exception.UserAlreadyExistsException;
import aprimorar.auth.exception.UserNotFoundException;
import aprimorar.auth.refresh.RefreshTokenService;
import aprimorar.common.utils.EmailUtils;
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
        String email = EmailUtils.normalize(request.email());

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
    public List<UserListResponse> findAll() {
        return userRepository.findAll()
            .stream()
            .map(UserListResponse::from)
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
        refreshTokenService.revokeAll(user.getId());
        userRepository.delete(user);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }
}
