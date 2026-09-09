package aprimorar.atendimentos.individuais.repository;


import aprimorar.atendimentos.individuais.domain.AtendimentoEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AtendimentoRepository extends JpaRepository<AtendimentoEntity, Long>, JpaSpecificationExecutor<AtendimentoEntity> {

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoEntity a
            WHERE a.alunoId = :alunoId
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
            FROM AtendimentoEntity a
            WHERE a.colaboradorId = :colaboradorId
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

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoEntity a
            WHERE a.alunoId = :alunoId
              AND a.status = aprimorar.atendimentos.individuais.domain.StatusAtendimento.AGENDADO
        """
    )
    boolean alunoPossuiAtendimentoAgendado(@Param("alunoId") UUID alunoId);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoEntity a
            WHERE a.alunoId = :alunoId
              AND a.status = aprimorar.atendimentos.individuais.domain.StatusAtendimento.CONCLUIDO
              AND a.dataPagamentoAluno IS NULL
        """
    )
    boolean alunoPossuiPagamentoAlunoPendente(@Param("alunoId") UUID alunoId);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoEntity a
            WHERE a.colaboradorId = :colaboradorId
              AND a.status = aprimorar.atendimentos.individuais.domain.StatusAtendimento.AGENDADO
        """
    )
    boolean colaboradorPossuiAtendimentoAgendado(@Param("colaboradorId") UUID colaboradorId);

    @Query(
        """
            SELECT count(a) > 0
            FROM AtendimentoEntity a
            WHERE a.colaboradorId = :colaboradorId
              AND a.status = aprimorar.atendimentos.individuais.domain.StatusAtendimento.CONCLUIDO
              AND a.dataRepasseColaborador IS NULL
        """
    )
    boolean colaboradorPossuiRepassePendente(@Param("colaboradorId") UUID colaboradorId);


}
