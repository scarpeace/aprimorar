package aprimorar.pessoas.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.pessoas.domain.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, UUID>, JpaSpecificationExecutor<Aluno> {

    List<Aluno> findAllByResponsavelId(UUID responsavelId);

    java.util.List<Aluno> findByNomeContainingIgnoreCase(String nome);

    boolean existsByResponsavelId(UUID responsavelId);

    boolean existsByResponsavelIdAndActiveTrue(UUID responsavelId);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndActiveFalse(UUID id);


    long countByActiveTrueAndIdNot(UUID id);

    long countByIdNot(UUID id);
}
