package aprimorar.pessoas.colaborador.internal.infrastructure.persistence;

import aprimorar.pessoas.colaborador.internal.domain.Colaborador;
import aprimorar.pessoas.colaborador.api.ColaboradorDutyEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID>, JpaSpecificationExecutor<Colaborador> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByDutyNotAndActiveTrue(ColaboradorDutyEnum duty);

    long countByDutyNot(ColaboradorDutyEnum duty);

    List<Colaborador> findAllByDutyNotAndActiveTrueOrderByNameAsc(ColaboradorDutyEnum duty);

    long countByActiveTrue();
}
