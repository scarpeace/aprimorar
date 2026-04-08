package com.aprimorar.api.domain.student.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aprimorar.api.domain.student.Student;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {

    Page<Student> findAllByParentId(UUID parentId, Pageable pageable);

    boolean existsByParentId(UUID parentId);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndArchivedAtIsNotNull(UUID id);
}
