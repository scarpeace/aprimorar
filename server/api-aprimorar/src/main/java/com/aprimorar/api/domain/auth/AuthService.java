package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.repository.StaffAccountRepository;
import java.time.Clock;
import java.time.Instant;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final StaffAccountRepository staffAccountRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final Clock applicationClock;

    public AuthService(
        StaffAccountRepository staffAccountRepository,
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
        Clock applicationClock
    ) {
        this.staffAccountRepository = staffAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationClock = applicationClock;
    }

    @Transactional
    public AuthCurrentUserResponseDTO login(String identifier, String password) {
        StaffAccount staffAccount = findByIdentifierOrThrow(identifier);

        if (!staffAccount.isActive()) {
            throw new DisabledException("Usuário interno inativo");
        }

        if (!passwordEncoder.matches(password, staffAccount.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        staffAccount.registerLoginAt(Instant.now(applicationClock));
        staffAccountRepository.save(staffAccount);

        return getCurrentUser(staffAccount);
    }

    @Transactional
    public AuthCurrentUserResponseDTO registerAuthenticatedSession(String username) {
        StaffAccount staffAccount = findByIdentifierOrThrow(username);
        staffAccount.registerLoginAt(Instant.now(applicationClock));
        staffAccountRepository.save(staffAccount);
        return getCurrentUser(staffAccount);
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(String username) {
        return getCurrentUser(findByIdentifierOrThrow(username));
    }

    @Transactional(readOnly = true)
    public AuthCurrentUserResponseDTO getCurrentUser(StaffAccount staffAccount) {
        return new AuthCurrentUserResponseDTO(
            staffAccount.getId(),
            staffAccount.getUsername(),
            staffAccount.getEmployee().getName(),
            staffAccount.getEmployee().getEmail(),
            staffAccount.getEmployee().getId(),
            staffAccount.getEmployee().getDuty()
        );
    }

    private StaffAccount findByIdentifierOrThrow(String identifier) {
        return staffAccountRepository.findByUsernameOrEmployeeEmail(identifier)
            .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }
}
