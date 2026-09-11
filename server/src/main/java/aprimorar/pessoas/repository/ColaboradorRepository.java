package aprimorar.pessoas.repository;

import aprimorar.pessoas.domain.ColaboradorEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, UUID>, JpaSpecificationExecutor<ColaboradorEntity> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("SELECT c.nome FROM ColaboradorEntity c WHERE c.id = :id")
    Optional<String> getNomeById(UUID id);

}
