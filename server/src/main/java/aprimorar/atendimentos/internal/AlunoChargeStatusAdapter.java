package aprimorar.atendimentos.internal;

import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.pessoas.aluno.api.AlunoChargeStatusApi;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AlunoChargeStatusAdapter implements AlunoChargeStatusApi {

    private final AtendimentoRepository appointmentRepository;

    public AlunoChargeStatusAdapter(AtendimentoRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingStudentCharges(UUID studentId) {
        return appointmentRepository.existsByStudentIdAndStudentChargeDateIsNull(studentId);
    }
}
