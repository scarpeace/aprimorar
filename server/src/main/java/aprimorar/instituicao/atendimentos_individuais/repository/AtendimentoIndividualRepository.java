package aprimorar.instituicao.atendimentos_individuais.repository;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.instituicao.atendimentos_individuais.domain.AtendimentoIndividual;

public interface AtendimentoIndividualRepository
    extends JpaRepository<AtendimentoIndividual, Long>, JpaSpecificationExecutor<AtendimentoIndividual> {

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Page<AtendimentoIndividual> findAll(Specification<AtendimentoIndividual> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    List<AtendimentoIndividual> findAll(Specification<AtendimentoIndividual> specification, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Optional<AtendimentoIndividual> findById(Long id);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoIndividual a
            WHERE a.aluno.id = :alunoId
              AND a.dataHoraInicio < :fim
              AND a.dataHoraFim > :inicio
              AND (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean alunoPossuiAtendimentoConflitante(
        @Param("alunoId") UUID alunoId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") Long ignoredAtendimentoId
    );

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoIndividual a
            WHERE a.colaborador.id = :colaboradorId
              AND a.dataHoraInicio < :fim
              AND a.dataHoraFim > :inicio
              AND (:ignoredAtendimentoId is null or a.id <> :ignoredAtendimentoId)
        """
    )
    boolean colaboradorPossuiAtendimentoConflitante(
        @Param("colaboradorId") UUID colaboradorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("ignoredAtendimentoId") Long ignoredAtendimentoId
    );

}
