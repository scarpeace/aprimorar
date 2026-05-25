package aprimorar.pessoas.responsavel.internal.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.pessoas.responsavel.internal.domain.Responsavel;

public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID>, JpaSpecificationExecutor<Responsavel> {


    Optional<Responsavel> findByCpf(String cpf);
    Optional<Responsavel> findByEmail(String email);

    List<Responsavel> findByActiveTrueOrderByNameAsc();

    Boolean existsByEmail(String cpf);
    Boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
