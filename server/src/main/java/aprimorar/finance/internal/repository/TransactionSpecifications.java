package aprimorar.finance.internal.repository;

import aprimorar.finance.internal.Transaction;
import aprimorar.finance.api.enums.TransactionCategory;

import aprimorar.finance.api.enums.TransactionOrigin;

import aprimorar.finance.api.enums.TransactionStatus;

import aprimorar.finance.api.enums.TransactionType;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

public final class TransactionSpecifications {

    private TransactionSpecifications() {
    }

    public static Specification<Transaction> withOrigin(TransactionOrigin origin) {
        return (root, query, cb) -> cb.equal(root.get("origin"), origin);
    }

    public static Specification<Transaction> withCategory(TransactionCategory category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Transaction> withType(TransactionType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> withStatus(TransactionStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Transaction> withSettledAtAfter(Instant startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("settledAt"), startDate);
    }

    public static Specification<Transaction> withSettledAtBefore(Instant endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("settledAt"), endDate);
    }
}
