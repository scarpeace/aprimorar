package com.aprimorar.api.domain.auth.internal.repository;

import com.aprimorar.api.domain.auth.internal.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmployeeId(UUID employeeId);
}
