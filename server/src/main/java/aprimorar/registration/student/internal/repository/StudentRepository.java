package aprimorar.registration.student.internal.repository;

import aprimorar.registration.student.internal.Student;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {

    Page<Student> findAllByParentId(UUID parentId, Pageable pageable);

    java.util.List<Student> findByNameContainingIgnoreCase(String name);

    boolean existsByParentId(UUID parentId);

    boolean existsByParentIdAndArchivedAtIsNull(UUID parentId);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndArchivedAtIsNotNull(UUID id);
}
