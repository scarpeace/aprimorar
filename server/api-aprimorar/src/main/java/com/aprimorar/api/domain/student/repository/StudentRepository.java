package com.aprimorar.api.domain.student.repository;

import com.aprimorar.api.domain.student.Student;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {

    @Override
    @EntityGraph(attributePaths = "parent")
    Optional<Student> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = "parent")
    Page<Student> findAll(Specification<Student> spec, Pageable pageable);

    @EntityGraph(attributePaths = "parent")
    Page<Student> findAllByParentId(UUID parentId, Pageable pageable);

    boolean existsByParentId(UUID parentId);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndArchivedAtIsNotNull(UUID id);
}
