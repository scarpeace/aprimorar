package com.aprimorar.api.domain.parent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {

    Optional<Parent> findByCpf(String cpf);
    Optional<Parent> findByEmail(String email);

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
