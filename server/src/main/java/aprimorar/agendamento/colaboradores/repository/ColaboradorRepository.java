package aprimorar.agendamento.colaboradores.repository;

import aprimorar.agendamento.colaboradores.domain.Colaborador;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID>, JpaSpecificationExecutor<Colaborador> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("SELECT c.nome FROM Colaborador c WHERE c.id = :id")
    Optional<String> getNomeById(UUID id);

}
