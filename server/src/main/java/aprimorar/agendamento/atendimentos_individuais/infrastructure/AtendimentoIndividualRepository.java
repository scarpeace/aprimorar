package aprimorar.agendamento.atendimentos_individuais.infrastructure;



import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aprimorar.agendamento.atendimentos_individuais.domain.AtendimentoIndividual;

public interface AtendimentoIndividualRepository
    extends JpaRepository<AtendimentoIndividual, Long>, JpaSpecificationExecutor<AtendimentoIndividual> {

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Page<AtendimentoIndividual> findAll(Specification<AtendimentoIndividual> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"aluno", "colaborador"})
    Optional<AtendimentoIndividual> findById(Long id);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoIndividual a
            WHERE a.aluno.id = :alunoId
              AND a.status <> aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual.CANCELADO
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
              AND a.status <> aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual.CANCELADO
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
