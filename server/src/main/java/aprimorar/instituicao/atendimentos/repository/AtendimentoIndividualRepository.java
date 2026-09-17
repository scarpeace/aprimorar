package aprimorar.instituicao.atendimentos.repository;



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

import aprimorar.instituicao.atendimentos.domain.AtendimentoIndividualEntity;

public interface AtendimentoIndividualRepository
    extends JpaRepository<AtendimentoIndividualEntity, Long>, JpaSpecificationExecutor<AtendimentoIndividualEntity> {

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Page<AtendimentoIndividualEntity> findAll(Specification<AtendimentoIndividualEntity> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    List<AtendimentoIndividualEntity> findAll(Specification<AtendimentoIndividualEntity> specification, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Optional<AtendimentoIndividualEntity> findById(Long id);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoIndividualEntity a
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
            FROM AtendimentoIndividualEntity a
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
