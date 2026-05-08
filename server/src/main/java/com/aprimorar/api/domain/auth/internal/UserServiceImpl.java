package com.aprimorar.api.domain.auth.internal;

import com.aprimorar.api.domain.auth.api.UserService;
import com.aprimorar.api.domain.auth.api.dto.UserCreateRequestDTO;
import com.aprimorar.api.domain.auth.api.dto.UserResponseDTO;
import com.aprimorar.api.domain.auth.api.exception.UserAlreadyExistsException;
import com.aprimorar.api.domain.auth.api.exception.UserNotFoundException;
import com.aprimorar.api.domain.auth.internal.repository.UserRepository;
import com.aprimorar.api.domain.employee.api.EmployeeService;
import com.aprimorar.api.domain.employee.api.dto.EmployeeResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectProvider<EmployeeService> employeeService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
        UserRepository userRepository,
        ObjectProvider<EmployeeService> employeeService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
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
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Nome de usuário já está em uso");
        }

        EmployeeResponseDTO employeeResponse = employeeService.getObject().getReferenceById(request.employeeId());

        User user = new User(
            employeeResponse.id(),
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

    @Transactional
    public void deleteByEmployeeId(UUID employeeId) {
        userRepository.findByEmployeeId(employeeId)
            .ifPresent(userRepository::delete);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmployeeId(UUID employeeId) {
        return userRepository.findByEmployeeId(employeeId)
            .map(this::mapToResponse)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    private UserResponseDTO mapToResponse(User user) {
        EmployeeResponseDTO employee = employeeService.getObject().getReferenceById(user.getEmployeeId());

        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            employee.name(),
            user.getRole(),
            user.isActive()
        );
    }
}
