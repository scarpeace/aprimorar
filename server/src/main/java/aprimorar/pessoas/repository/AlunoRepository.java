package aprimorar.pessoas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import aprimorar.pessoas.domain.AlunoEntity;

public interface AlunoRepository extends JpaRepository<AlunoEntity, UUID>, JpaSpecificationExecutor<AlunoEntity> {

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("SELECT a.nome FROM AlunoEntity a WHERE a.id = :id")
    Optional<String> getNomeById(UUID id);
}
