package aprimorar.atendimentos.internal.repository;

import aprimorar.atendimentos.internal.Atendimento;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<Atendimento> contentContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            if (term == null || term.trim().isEmpty()) {
                return null;
            }
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("content").as(String.class)), pattern);
        };
    }

    public static Specification<Atendimento> searchContains(String term) {
        return (root, query, cb) -> {
            if (term == null || term.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + term.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("studentName")), pattern),
                cb.like(cb.lower(root.get("employeeName")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("content").as(String.class)), pattern)
            );
        };
    }

    public static Specification<Atendimento> withStartDateAfter(Instant startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Atendimento> withEndDateBefore(Instant endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    public static Specification<Atendimento> withStudentId(UUID studentId) {
        return (root, query, cb) -> studentId == null ? null : cb.equal(root.get("studentId"), studentId);
    }

    public static Specification<Atendimento> withEmployeeId(UUID employeeId) {
        return (root, query, cb) -> employeeId == null ? null : cb.equal(root.get("employeeId"), employeeId);
    }

    public static Specification<Atendimento> withStudentIds(List<UUID> studentIds) {
        return (root, query, cb) -> {
            if (studentIds == null) {
                return null;
            }
            if (studentIds.isEmpty()) {
                return cb.disjunction();
            }
            return root.get("studentId").in(studentIds);
        };
    }

    public static Specification<Atendimento> withEmployeePaid(Boolean paid) {
        return (root, query, cb) -> {
            if (paid == null) return null;
            return paid ? cb.isNotNull(root.get("employeePaymentDate")) : cb.isNull(root.get("employeePaymentDate"));
        };
    }

    public static Specification<Atendimento> withStudentCharged(Boolean charged) {
        return (root, query, cb) -> {
            if (charged == null) return null;
            return charged ? cb.isNotNull(root.get("studentChargeDate")) : cb.isNull(root.get("studentChargeDate"));
        };
    }

    public static Specification<Atendimento> withEmployeeIds(List<UUID> employeeIds) {
        return (root, query, cb) -> {
            if (employeeIds == null) {
                return null;
            }
            if (employeeIds.isEmpty()) {
                return cb.disjunction();
            }
            return root.get("employeeId").in(employeeIds);
        };
    }
}
