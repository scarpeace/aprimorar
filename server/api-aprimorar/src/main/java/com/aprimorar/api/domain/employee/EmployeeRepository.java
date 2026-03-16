package com.aprimorar.api.domain.employee;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndArchivedAtIsNotNull(UUID id);
}
