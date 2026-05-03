package com.aprimorar.api.domain.parent.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aprimorar.api.domain.parent.Parent;

public interface ParentRepository extends JpaRepository<Parent, UUID>, JpaSpecificationExecutor<Parent> {


    Optional<Parent> findByCpf(String cpf);
    Optional<Parent> findByEmail(String email);

    List<Parent> findByArchivedAtIsNullOrderByNameAsc();

    Boolean existsByEmail(String cpf);
    Boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
