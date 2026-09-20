package aprimorar.financeiro.financeiro_aluno.cobrancas.repository;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto.CobrancaAlunoFiltroRequest;
import aprimorar.financeiro.common.FormaPagamentoEnum;

import jakarta.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CobrancaAlunoSpecifications {

    private CobrancaAlunoSpecifications() {
    }

    public static Specification<CobrancaAluno> comFiltros(
        CobrancaAlunoFiltroRequest filtro
    ) {
        return Specification.allOf(
            alunoIdIgual(filtro.alunoId()),
            statusIgual(filtro.status()),
            formaPagamentoDoPagamentoIgual(filtro.formaPagamento()),
            dataPagamentoDoPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()),
            dataPagamentoDoPagamentoMenorOuIgual(filtro.dataPagamentoFim())
        );
    }

    private static Specification<CobrancaAluno> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<CobrancaAluno> statusIgual(StatusCobrancaAluno status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<CobrancaAluno> formaPagamentoDoPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> {
            if (formaPagamento == null) {
                return null;
            }
            query.distinct(true);
            return cb.equal(
                root.join("parcelas", JoinType.LEFT)
                    .join("pagamento", JoinType.LEFT)
                    .get("formaPagamento"),
                formaPagamento
            );
        };
    }

    private static Specification<CobrancaAluno> dataPagamentoDoPagamentoMaiorOuIgual(LocalDate data) {
        return (root, query, cb) -> {
            if (data == null) {
                return null;
            }
            query.distinct(true);
            return cb.greaterThanOrEqualTo(
                root.join("parcelas", JoinType.LEFT)
                    .join("pagamento", JoinType.LEFT)
                    .get("dataPagamento"),
                data
            );
        };
    }

    private static Specification<CobrancaAluno> dataPagamentoDoPagamentoMenorOuIgual(LocalDate data) {
        return (root, query, cb) -> {
            if (data == null) {
                return null;
            }
            query.distinct(true);
            return cb.lessThanOrEqualTo(
                root.join("parcelas", JoinType.LEFT)
                    .join("pagamento", JoinType.LEFT)
                    .get("dataPagamento"),
                data
            );
        };
    }

}
