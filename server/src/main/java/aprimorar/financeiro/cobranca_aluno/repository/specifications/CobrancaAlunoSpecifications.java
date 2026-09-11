package aprimorar.financeiro.cobranca_aluno.repository.specifications;

import aprimorar.financeiro.cobranca_aluno.api.FormaPagamento;
import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import aprimorar.financeiro.cobranca_aluno.domain.StatusCobrancaAluno;
import aprimorar.financeiro.cobranca_aluno.web.dto.CobrancaAlunoFiltroRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CobrancaAlunoSpecifications {

    private CobrancaAlunoSpecifications() {}

    public static Specification<CobrancaAlunoEntity> comFiltros(
        CobrancaAlunoFiltroRequest filtro
    ) {
        return Specification
            .where(alunoIgual(filtro.alunoId()))
            .and(statusIgual(filtro.status()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()))
            .and(dataPagamentoMenorOuIgual(filtro.dataPagamentoFim()));
    }

    public static Specification<CobrancaAlunoEntity> alunoIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null
            ? null
            : cb.equal(root.get("alunoId"), alunoId);
    }

    public static Specification<CobrancaAlunoEntity> statusIgual(StatusCobrancaAluno status) {
        return (root, query, cb) -> status == null
            ? null
            : cb.equal(root.get("status"), status);
    }

    public static Specification<CobrancaAlunoEntity> formaPagamentoIgual(FormaPagamento formaPagamento) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    public static Specification<CobrancaAlunoEntity> dataPagamentoMaiorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.<LocalDateTime>get("dataPagamento"),
                data.atStartOfDay()
            );
    }

    public static Specification<CobrancaAlunoEntity> dataPagamentoMenorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.lessThanOrEqualTo(
                root.<LocalDateTime>get("dataPagamento"),
                data.atTime(LocalTime.MAX)
            );
    }
}
