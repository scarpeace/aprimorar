package aprimorar.atendimentos.service;

import org.springframework.stereotype.Component;


@Component
public class AtendimentoEventListener {


    // public AtendimentoEventListener(
    //     AtendimentoRepository atendimentoRepo,
    //     AlunoRepository alunoRepo,
    //     ColaboradorRepository colaboradorRepo
    // ) {
    //     this.atendimentoRepo = atendimentoRepo;
    //     this.alunoRepo = alunoRepo;
    //     this.colaboradorRepo = colaboradorRepo;
    // }

    // @EventListener
    // public void onAlunoDeleted(AlunoDeletedEvent event) {
    //     atendimentoRepo.reassignAtendimentosAlunoToGhost(event.studentId(), alunoRepo.getReferenceById(GHOST_STUDENT_ID));
    // }

    // @EventListener
    // public void onColaboradorDeleted(ColaboradorDeletedEvent event) {
    //     atendimentoRepo.reassignAtendimentosColaboradorToGhost(event.colaboradorId(), colaboradorRepo.getReferenceById(GHOST_COLABORADOR_ID));
    // }

}
