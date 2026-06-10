package aprimorar.pessoas.repository;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.shared.FuncoesColaborador;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID>, JpaSpecificationExecutor<Colaborador> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByDutyNotAndActiveTrue(FuncoesColaborador duty);

    long countByDutyNot(FuncoesColaborador duty);

    List<Colaborador> findAllByDutyNotAndActiveTrueOrderByNameAsc(FuncoesColaborador duty);

    long countByActiveTrueAndDutyNot(FuncoesColaborador role);
}
