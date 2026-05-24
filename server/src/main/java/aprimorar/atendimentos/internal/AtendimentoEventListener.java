package aprimorar.atendimentos.internal;

import aprimorar.pessoas.aluno.api.AlunoDeletedEvent;
import aprimorar.pessoas.colaborador.api.ColaboradorDeletedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoEventListener {

    private final AtendimentoMutationService atendimentoMutationService;

    public AtendimentoEventListener(AtendimentoMutationService atendimentoMutationService) {
        this.atendimentoMutationService = atendimentoMutationService;
    }

    @EventListener
    public void onStudentDeleted(AlunoDeletedEvent event) {
        atendimentoMutationService.reassignStudentAtendimentosToGhost(event.studentId());
    }

    @EventListener
    public void onEmployeeDeleted(ColaboradorDeletedEvent event) {
        atendimentoMutationService.onEmployeeDeleted(event.colaboradorId());
    }
}
