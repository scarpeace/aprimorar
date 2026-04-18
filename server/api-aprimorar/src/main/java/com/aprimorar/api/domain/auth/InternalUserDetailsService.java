package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.repository.InternalUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternalUserDetailsService implements UserDetailsService {

    private final InternalUserRepository internalUserRepository;

    public InternalUserDetailsService(InternalUserRepository internalUserRepository) {
        this.internalUserRepository = internalUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        InternalUser internalUser = internalUserRepository.findByUsernameOrEmployeeEmail(identifier)
            .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));

        return User.withUsername(internalUser.getUsername())
            .password(internalUser.getPasswordHash())
            .disabled(!internalUser.isActive())
            .authorities("ROLE_INTERNAL")
            .build();
    }
}
