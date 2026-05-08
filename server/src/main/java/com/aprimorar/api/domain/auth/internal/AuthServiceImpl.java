package com.aprimorar.api.domain.auth.internal;

import com.aprimorar.api.domain.auth.api.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.api.AuthService;
import com.aprimorar.api.domain.auth.internal.repository.UserRepository;
import com.aprimorar.api.domain.employee.api.EmployeeService;
import com.aprimorar.api.domain.employee.api.dto.EmployeeResponseDTO;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ObjectProvider<EmployeeService> employeeService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final Clock applicationClock;

    public AuthServiceImpl(
        UserRepository userRepository,
        ObjectProvider<EmployeeService> employeeService,
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
        Clock applicationClock
    ) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.applicationClock = applicationClock;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = findUserByIdentifier(identifier)
            .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .disabled(!user.isActive())
            .authorities("ROLE_" + user.getRole().name())
            .build();
    }

    @Transactional
    public AuthCurrentUserResponseDTO login(String identifier, String password) {
        User user = findByIdentifierOrThrow(identifier);

        if (!user.isActive()) {
            throw new DisabledException("Usuário interno inativo");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        user.registerLoginAt(Instant.now(applicationClock));
        userRepository.save(user);

        return getCurrentUser(user);
    }

    @Transactional
    public AuthCurrentUserResponseDTO registerAuthenticatedSession(String username) {
        User user = findByIdentifierOrThrow(username);
        user.registerLoginAt(Instant.now(applicationClock));
        userRepository.save(user);
        return getCurrentUser(user);
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(String username) {
        return getCurrentUser(findByIdentifierOrThrow(username));
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(User user) {
        EmployeeResponseDTO employee = employeeService.getObject().getReferenceById(user.getEmployeeId());

        return new AuthCurrentUserResponseDTO(
            user.getId(),
            user.getUsername(),
            employee.name(),
            employee.email(),
            employee.id(),
            employee.duty(),
            user.getRole()
        );
    }

    private User findByIdentifierOrThrow(String identifier) {
        return findUserByIdentifier(identifier)
            .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }

    private Optional<User> findUserByIdentifier(String identifier) {
        return userRepository.findByUsername(identifier)
            .or(() -> findUserByEmployeeEmail(identifier));
    }

    private Optional<User> findUserByEmployeeEmail(String identifier) {
        Optional<UUID> employeeId = employeeService.getObject().findIdByEmail(identifier);
        return employeeId.flatMap(userRepository::findByEmployeeId);
    }
}
