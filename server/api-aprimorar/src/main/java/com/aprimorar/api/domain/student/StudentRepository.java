package com.aprimorar.api.domain.student;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {

    List<Student> findAllByParentId(UUID parentId);

    boolean existsByParentId(UUID parentId);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndArchivedAtIsNotNull(UUID id);
}
