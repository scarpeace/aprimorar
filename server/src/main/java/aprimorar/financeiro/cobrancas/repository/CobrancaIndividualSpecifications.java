package aprimorar.financeiro.cobrancas.repository;

import aprimorar.financeiro.cobrancas.domain.CobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.cobrancas.web.dto.CobrancaIndividualFiltroRequest;
import aprimorar.financeiro.common.FormaPagamentoEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CobrancaIndividualSpecifications {

    private CobrancaIndividualSpecifications() {
    }

    public static Specification<CobrancaIndividual> comFiltros(
        CobrancaIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(alunoIdIgual(filtro.alunoId()))
            .and(statusIgual(filtro.status()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()))
            .and(dataPagamentoMenorOuIgual(filtro.dataPagamentoFim()));
    }

    private static Specification<CobrancaIndividual> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<CobrancaIndividual> statusIgual(StatusCobrancaIndividual status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<CobrancaIndividual> formaPagamentoIgual(FormaPagamentoEnum formaPagamento) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<CobrancaIndividual> dataPagamentoMaiorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.greaterThanOrEqualTo(root.<LocalDateTime>get("dataPagamento"), data.atStartOfDay());
    }

    private static Specification<CobrancaIndividual> dataPagamentoMenorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.lessThanOrEqualTo(root.<LocalDateTime>get("dataPagamento"), data.atTime(LocalTime.MAX));
    }

}
