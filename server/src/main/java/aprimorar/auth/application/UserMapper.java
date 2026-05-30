package aprimorar.auth.application;

import aprimorar.auth.web.dto.UserRequestDTO;
import aprimorar.auth.web.dto.UserResponseDTO;
import aprimorar.auth.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto, String normalizedUsername, String encodedPassword) {
        return new User(normalizedUsername, encodedPassword, dto.role(), true);
    }

    public UserResponseDTO toDto(User user) {
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
