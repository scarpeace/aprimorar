package aprimorar.finance.internal.listener;

import aprimorar.appointment.api.event.EmployeePaymentToggledEvent;
import aprimorar.appointment.api.event.StudentChargeToggledEvent;
import aprimorar.finance.api.TransactionService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FinanceEventReactor {

    private final TransactionService transactionService;

    public FinanceEventReactor(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @EventListener
    public void onStudentChargeToggled(StudentChargeToggledEvent event) {
        transactionService.syncStudentCharge(event.appointmentId(), event.settledAt());
    }

    @EventListener
    public void onEmployeePaymentToggled(EmployeePaymentToggledEvent event) {
        transactionService.syncEmployeePayment(event.appointmentId(), event.settledAt());
    }
}
