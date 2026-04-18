package com.aprimorar.api.domain.auth.repository;

import com.aprimorar.api.domain.auth.InternalUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InternalUserRepository extends JpaRepository<InternalUser, UUID> {

    @EntityGraph(attributePaths = "employee")
    @Query(
        """
        select internalUser
        from InternalUser internalUser
        join internalUser.employee employee
        where internalUser.username = :identifier
           or employee.email = :identifier
        """
    )
    Optional<InternalUser> findByUsernameOrEmployeeEmail(@Param("identifier") String identifier);
}
