package aprimorar.financeiro.internal.infrastructure.persistence;

import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

import aprimorar.financeiro.api.CategoriaDespesaEnum;
import aprimorar.financeiro.internal.domain.Despesa;

public final class DespesaSpecifications {

    private DespesaSpecifications() {
    }

    public static Specification<Despesa> withCategory(CategoriaDespesaEnum category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Despesa> withDateFrom(Instant startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("date"), startDate);
    }

    public static Specification<Despesa> withDateTo(Instant endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("date"), endDate);
    }

    public static Specification<Despesa> withPendingPayment() {
        return (root, query, cb) -> cb.isNull(root.get("paymentDate"));
    }
}
