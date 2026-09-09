package aprimorar.pessoas.responsavel.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.pessoas.responsavel.domain.ResponsavelEntity;


public interface ResponsavelRepository extends JpaRepository<ResponsavelEntity, UUID>, JpaSpecificationExecutor<ResponsavelEntity> {


    Optional<ResponsavelEntity> findByCpf(String cpf);
    Optional<ResponsavelEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
