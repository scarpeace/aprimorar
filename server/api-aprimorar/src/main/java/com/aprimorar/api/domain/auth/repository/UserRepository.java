package com.aprimorar.api.domain.auth.repository;

import com.aprimorar.api.domain.auth.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = "employee")
    @Query(
        """
        select u
        from User u
        join u.employee employee
        where u.username = :identifier
           or employee.email = :identifier
        """
    )
    Optional<User> findByUsernameOrEmployeeEmail(@Param("identifier") String identifier);
}
