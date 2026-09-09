package aprimorar.pessoas.colaborador.repository;

import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.enums.FuncoesColaborador;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ColaboradorRepository extends JpaRepository<ColaboradorEntity, UUID>, JpaSpecificationExecutor<ColaboradorEntity> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByFuncaoNotAndActiveTrue(FuncoesColaborador funcao);

    long countByFuncaoNot(FuncoesColaborador funcao);

    List<ColaboradorEntity> findAllByFuncaoNotAndActiveTrueOrderByNomeAsc(FuncoesColaborador funcao);

    long countByActiveTrueAndFuncaoNot(FuncoesColaborador role);

    @Query("SELECT c.nome FROM ColaboradorEntity c WHERE c.id = :id")
    Optional<String> getNomeById(UUID id);

}
