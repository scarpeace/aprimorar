package aprimorar.financeiro.cobrancas.repository.specifications;

import aprimorar.financeiro.cobrancas.domain.CobrancaIndividualEntity;
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

    public static Specification<CobrancaIndividualEntity> comFiltros(
        CobrancaIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(alunoIdIgual(filtro.alunoId()))
            .and(statusIgual(filtro.status()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataPagamentoMaiorOuIgual(filtro.dataPagamentoInicio()))
            .and(dataPagamentoMenorOuIgual(filtro.dataPagamentoFim()));
    }

    private static Specification<CobrancaIndividualEntity> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<CobrancaIndividualEntity> statusIgual(StatusCobrancaIndividual status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<CobrancaIndividualEntity> formaPagamentoIgual(FormaPagamentoEnum formaPagamento) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    private static Specification<CobrancaIndividualEntity> dataPagamentoMaiorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.greaterThanOrEqualTo(root.<LocalDateTime>get("dataPagamento"), data.atStartOfDay());
    }

    private static Specification<CobrancaIndividualEntity> dataPagamentoMenorOuIgual(LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.lessThanOrEqualTo(root.<LocalDateTime>get("dataPagamento"), data.atTime(LocalTime.MAX));
    }

}
