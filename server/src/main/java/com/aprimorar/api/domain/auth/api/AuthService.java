package com.aprimorar.api.domain.auth.api;

import com.aprimorar.api.domain.auth.api.dto.AuthCurrentUserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    AuthCurrentUserResponseDTO login(String identifier, String password);

    AuthCurrentUserResponseDTO registerAuthenticatedSession(String username);

    AuthCurrentUserResponseDTO getCurrentUser(String username);
}
