package aprimorar.auth.usuario.service;

import aprimorar.auth.usuario.domain.User;
import aprimorar.auth.usuario.repository.UserRepository;
import aprimorar.common.utils.MapperUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        String normalizedUsername = MapperUtils.normalizeEmail(username);
        if (normalizedUsername == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return userRepository.findByUsername(normalizedUsername)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
