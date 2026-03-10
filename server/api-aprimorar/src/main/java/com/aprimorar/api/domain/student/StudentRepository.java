package com.aprimorar.api.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {

    Optional<StudentEntity> findByIdAndArchivedAtIsNull(UUID id);
}
