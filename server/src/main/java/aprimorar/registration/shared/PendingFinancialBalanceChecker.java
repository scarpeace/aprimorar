package aprimorar.registration.shared;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// TODO: Tem que deletar essa porra

@Component
public class PendingFinancialBalanceChecker {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public boolean hasPendingStudentCharges(UUID studentId) {
        Number pendingCount = (Number) entityManager.createNativeQuery(
            """
            select count(*)
            from tb_appointments a
            where a.student_id = :studentId
              and a.student_charge_date is null
            """
        )
            .setParameter("studentId", studentId)
            .getSingleResult();

        return pendingCount.longValue() > 0;
    }

    @Transactional(readOnly = true)
    public boolean hasPendingEmployeePayments(UUID employeeId) {
        Number pendingCount = (Number) entityManager.createNativeQuery(
            """
            select count(*)
            from tb_appointments a
            where a.employee_id = :employeeId
              and a.employee_payment_date is null
            """
        )
            .setParameter("employeeId", employeeId)
            .getSingleResult();

        return pendingCount.longValue() > 0;
    }
}
