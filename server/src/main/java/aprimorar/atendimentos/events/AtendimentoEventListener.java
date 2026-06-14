package aprimorar.atendimentos.events;

import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.pessoas.api.AlunoDeletedEvent;
import aprimorar.pessoas.api.ColaboradorDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AtendimentoEventListener {

    private final AtendimentoRepository atendimentoRepo;
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");
    private static final UUID GHOST_COLABORADOR_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");

    public AtendimentoEventListener(AtendimentoRepository atendimentoRepo) {
        this.atendimentoRepo = atendimentoRepo;
    }

    @EventListener
    public void onAlunoDeleted(AlunoDeletedEvent event) {
        atendimentoRepo.reassignAtendimentosAlunoToGhost(event.studentId(), GHOST_STUDENT_ID);
    }

    @EventListener
    public void onColaboradorDeleted(ColaboradorDeletedEvent event) {
        atendimentoRepo.reassignAtendimentosColaboradorToGhost(event.colaboradorId(), GHOST_COLABORADOR_ID);
    }

}
