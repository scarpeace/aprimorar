package aprimorar.pessoas.aluno.internal.infrastructure.persistence;

import aprimorar.pessoas.aluno.internal.domain.Aluno;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlunoRepository extends JpaRepository<Aluno, UUID>, JpaSpecificationExecutor<Aluno> {

    List<Aluno> findAllByParentId(UUID parentId);

    java.util.List<Aluno> findByNameContainingIgnoreCase(String name);

    boolean existsByParentId(UUID parentId);

    boolean existsByParentIdAndActiveTrue(UUID parentId);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndActiveFalse(UUID id);

    long countByActiveTrue();
}
