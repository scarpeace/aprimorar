package aprimorar.pessoas.colaborador.internal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.pessoas.colaborador.api.contract.DutyEnum;
import aprimorar.pessoas.colaborador.internal.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID>, JpaSpecificationExecutor<Colaborador> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByDutyNotAndActiveTrue(DutyEnum duty);

    long countByDutyNot(DutyEnum duty);

    List<Colaborador> findAllByDutyNotAndActiveTrueOrderByNameAsc(DutyEnum duty);
}
