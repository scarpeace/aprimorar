package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.dto.UserCreateRequestDTO;
import com.aprimorar.api.domain.auth.dto.UserResponseDTO;
import com.aprimorar.api.domain.auth.exception.UserAlreadyExistsException;
import com.aprimorar.api.domain.auth.exception.UserNotFoundException;
import com.aprimorar.api.domain.auth.repository.UserRepository;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        EmployeeRepository employeeRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional
    public UserResponseDTO create(UserCreateRequestDTO request) {
        if (userRepository.findByUsernameOrEmployeeEmail(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Nome de usuário já está em uso");
        }

        Employee employee = employeeRepository.findById(request.employeeId())
            .orElseThrow(() -> new EmployeeNotFoundException("Funcionário não encontrado"));

        User user = new User(
            employee,
            request.username(),
            passwordEncoder.encode(request.password()),
            true,
            request.role()
        );

        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        
        userRepository.delete(user);
    }

    private UserResponseDTO mapToResponse(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmployee().getName(),
            user.getRole(),
            user.isActive()
        );
    }
}
