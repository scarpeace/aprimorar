package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.repository.StaffAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffAccountDetailsService implements UserDetailsService {

    private final StaffAccountRepository staffAccountRepository;

    public StaffAccountDetailsService(StaffAccountRepository staffAccountRepository) {
        this.staffAccountRepository = staffAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        StaffAccount staffAccount = staffAccountRepository.findByUsernameOrEmployeeEmail(identifier)
            .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));

        return User.withUsername(staffAccount.getUsername())
            .password(staffAccount.getPasswordHash())
            .disabled(!staffAccount.isActive())
            .authorities("ROLE_INTERNAL")
            .build();
    }
}
