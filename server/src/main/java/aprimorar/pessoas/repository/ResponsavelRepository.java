package aprimorar.pessoas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.pessoas.domain.Responsavel;


public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID>, JpaSpecificationExecutor<Responsavel> {


    Optional<Responsavel> findByCpf(String cpf);
    Optional<Responsavel> findByEmail(String email);

    Boolean existsByEmail(String cpf);
    Boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
