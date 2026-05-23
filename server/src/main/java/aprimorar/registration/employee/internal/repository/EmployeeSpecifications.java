package aprimorar.registration.employee.internal.repository;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.registration.employee.api.Duty;
import aprimorar.registration.employee.internal.Employee;

public final class EmployeeSpecifications {
    private EmployeeSpecifications() {
    }

    public static Specification<Employee> isNotArchived() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Employee> isArchived() {
        return (root, query, cb) -> cb.isFalse(root.get("active"));
    }

    public static Specification<Employee> isNotGhost() {
        return (root, query, cb) -> cb.notEqual(root.get("duty"), Duty.SYSTEM);
    }

    public static Specification<Employee> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String pattern = "%" + term.toLowerCase() + "%";
            return cb.and(
                    cb.notEqual(root.get("duty"), Duty.SYSTEM),
                    cb.or(
                            cb.like(cb.lower(root.get("name")), pattern),
                            cb.like(cb.lower(root.get("email")), pattern),
                            cb.like(cb.lower(root.get("duty").as(String.class)), pattern)
                    )
            );
        };
    }
}
