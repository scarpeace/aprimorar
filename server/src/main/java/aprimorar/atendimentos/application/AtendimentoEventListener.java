package aprimorar.atendimentos.application;

import aprimorar.atendimentos.infrastructure.persistence.AtendimentoRepository;
import aprimorar.pessoas.aluno.AlunoDeletedEvent;
import aprimorar.pessoas.aluno.ArchiveAlunoVerificationEvent;
import aprimorar.pessoas.aluno.DeleteAlunoVerificationEvent;
import aprimorar.pessoas.colaborador.ArchiveColaboradorVerificationEvent;
import aprimorar.pessoas.colaborador.ColaboradorDeletedEvent;

import aprimorar.pessoas.colaborador.DeleteColaboradorVerificationEvent;
import aprimorar.shared.exception.BusinessException;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AtendimentoEventListener {

//    TODO: Tem que ter um lugar pra colocar essas variáveis, não é possível.
    private final AtendimentoRepository atendimentoRepo;
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID GHOST_COLABORADOR_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");


    public AtendimentoEventListener(AtendimentoRepository atendimentoRepo) {
        this.atendimentoRepo = atendimentoRepo;
    }

    @EventListener
    public void onAlunoDeleted(AlunoDeletedEvent event) {
        atendimentoRepo.reassingAtendimentoAlunosToGhost(event.studentId(), GHOST_STUDENT_ID);
    }

    @EventListener
    public void onColaboradorDeleted(ColaboradorDeletedEvent event) {
        atendimentoRepo.reassignAtendimentosColaboradorToGhost(event.colaboradorId(), GHOST_COLABORADOR_ID);
    }

    @EventListener
    public void deleteAlunoVerification(DeleteAlunoVerificationEvent event){
        if (atendimentoRepo.existsByStudentIdAndStudentChargeDateIsNull(event.uuid())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível excluir um aluno com cobranças pendentes");
        }
    }

    @EventListener
    public void archiveAlunoVerification(ArchiveAlunoVerificationEvent event){
        if (atendimentoRepo.existsByStudentIdAndStudentChargeDateIsNull(event.uuid())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível arquivar um aluno com cobranças pendentes");
        }
    }

    @EventListener
    public void deleteColaboradorVerification(DeleteColaboradorVerificationEvent event){
           if (atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(event.uuid())){
               throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível excluir um colaborador com pagamentos pendentes");
           }
    }

    @EventListener
    public void archiveColaboradorVerification(ArchiveColaboradorVerificationEvent event){
        if (atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(event.uuid())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível arquivar um colaborador com pagamentos pendentes");
        }
    }
}
