package com.aprimorar.api.domain.student;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {

    Optional<StudentEntity> findByIdAndArchivedAtIsNull(UUID id);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
