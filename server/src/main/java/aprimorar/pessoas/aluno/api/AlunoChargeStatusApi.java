package aprimorar.pessoas.aluno.api;

import java.util.UUID;

public interface AlunoChargeStatusApi {

    boolean hasPendingStudentCharges(UUID studentId);
}
