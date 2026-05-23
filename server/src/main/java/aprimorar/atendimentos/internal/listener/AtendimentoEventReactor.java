package aprimorar.atendimentos.internal.listener;

import aprimorar.atendimentos.api.AtendimentoService;
import aprimorar.pessoas.colaborador.api.event.ColaboradorDeletedEvent;
import aprimorar.pessoas.aluno.api.event.AlunoDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoEventReactor {

    private final AtendimentoService appointmentService;

    public AtendimentoEventReactor(AtendimentoService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @EventListener
    public void onStudentDeleted(AlunoDeletedEvent event) {
        appointmentService.reassignStudentAppointmentsToGhost(event.studentId());
    }

    @EventListener
    public void onEmployeeDeleted(ColaboradorDeletedEvent event) {
        appointmentService.reassignEmployeeAppointmentsToGhost(event.colaboradorId());
    }
}
