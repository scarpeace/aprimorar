package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.repository.UserRepository;
import java.time.Clock;
import java.time.Instant;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final Clock applicationClock;

    public AuthService(
        UserRepository userRepository,
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
        Clock applicationClock
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationClock = applicationClock;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmployeeEmail(identifier)
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
        return new AuthCurrentUserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmployee().getName(),
            user.getEmployee().getEmail(),
            user.getEmployee().getId(),
            user.getEmployee().getDuty(),
            user.getRole()
        );
    }

    private User findByIdentifierOrThrow(String identifier) {
        return userRepository.findByUsernameOrEmployeeEmail(identifier)
            .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }
}
