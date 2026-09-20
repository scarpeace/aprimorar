package aprimorar.financeiro.financeiro_aluno.pagamentos.repository;

import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.PagamentoAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.repository.projections.PagamentoAlunoProjection;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagamentoAlunoRepository
    extends JpaRepository<PagamentoAluno, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PagamentoAluno p where p.id = :pagamentoId")
    Optional<PagamentoAluno> findByIdForUpdate(@Param("pagamentoId") UUID pagamentoId);

    @Query(
        value = """
            select
                p.id as id,
                p.alunoId as alunoId,
                p.dataPagamento as dataPagamento,
                p.formaPagamento as formaPagamento,
                p.comprovanteUrl as comprovanteUrl,
                sum(c.valor) as valorTotal,
                count(c.id) as quantidadeCobrancas
            from CobrancaAluno c
            join c.pagamento p
            where p.alunoId = :alunoId
            group by
                p.id,
                p.alunoId,
                p.dataPagamento,
                p.formaPagamento,
                p.comprovanteUrl
            order by p.dataPagamento desc
            """,
        countQuery = """
            select count(distinct p.id)
            from CobrancaAluno c
            join c.pagamento p
            where p.alunoId = :alunoId
            """
    )
    Page<PagamentoAlunoProjection> findPagamentosPorAlunoId(
        @Param("alunoId") UUID alunoId,
        Pageable pageable
    );
}
