package aprimorar.registration.shared;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PendingFinancialBalanceChecker {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public boolean hasPendingStudentCharges(UUID studentId) {
        Number pendingCount = (Number) entityManager.createNativeQuery(
            """
            select count(*)
            from tb_transactions t
            join tb_appointments a on a.id = t.origin_id
            where t.origin = 'APPOINTMENT_STUDENT_CHARGE'
              and t.status = 'PENDING'
              and a.student_id = :studentId
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
            from tb_transactions t
            join tb_appointments a on a.id = t.origin_id
            where t.origin = 'APPOINTMENT_EMPLOYEE_PAYMENT'
              and t.status = 'PENDING'
              and a.employee_id = :employeeId
            """
        )
            .setParameter("employeeId", employeeId)
            .getSingleResult();

        return pendingCount.longValue() > 0;
    }
}
