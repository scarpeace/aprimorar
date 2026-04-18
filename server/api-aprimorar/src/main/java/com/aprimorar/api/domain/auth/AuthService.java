package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.repository.InternalUserRepository;
import java.time.Clock;
import java.time.Instant;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final InternalUserRepository internalUserRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final Clock applicationClock;

    public AuthService(
        InternalUserRepository internalUserRepository,
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
        Clock applicationClock
    ) {
        this.internalUserRepository = internalUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationClock = applicationClock;
    }

    @Transactional
    public AuthCurrentUserResponseDTO login(String identifier, String password) {
        InternalUser internalUser = findByIdentifierOrThrow(identifier);

        if (!internalUser.isActive()) {
            throw new DisabledException("Usuário interno inativo");
        }

        if (!passwordEncoder.matches(password, internalUser.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        internalUser.registerLoginAt(Instant.now(applicationClock));
        internalUserRepository.save(internalUser);

        return getCurrentUser(internalUser);
    }

    @Transactional
    public AuthCurrentUserResponseDTO registerAuthenticatedSession(String username) {
        InternalUser internalUser = findByIdentifierOrThrow(username);
        internalUser.registerLoginAt(Instant.now(applicationClock));
        internalUserRepository.save(internalUser);
        return getCurrentUser(internalUser);
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(String username) {
        return getCurrentUser(findByIdentifierOrThrow(username));
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(InternalUser internalUser) {
        return new AuthCurrentUserResponseDTO(
            internalUser.getId(),
            internalUser.getUsername(),
            internalUser.getEmployee().getName(),
            internalUser.getEmployee().getEmail(),
            internalUser.getEmployee().getId(),
            internalUser.getEmployee().getDuty()
        );
    }

    private InternalUser findByIdentifierOrThrow(String identifier) {
        return internalUserRepository.findByUsernameOrEmployeeEmail(identifier)
            .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }
}
