package com.aprimorar.api.domain.auth.repository;

import com.aprimorar.api.domain.auth.StaffAccount;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffAccountRepository extends JpaRepository<StaffAccount, UUID> {

    @EntityGraph(attributePaths = "employee")
    @Query(
        """
        select staffAccount
        from StaffAccount staffAccount
        join staffAccount.employee employee
        where staffAccount.username = :identifier
           or employee.email = :identifier
        """
    )
    Optional<StaffAccount> findByUsernameOrEmployeeEmail(@Param("identifier") String identifier);
}
