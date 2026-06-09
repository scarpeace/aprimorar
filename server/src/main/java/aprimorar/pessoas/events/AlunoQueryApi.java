package aprimorar.pessoas.events;

import java.util.List;
import java.util.UUID;

public interface AlunoQueryApi {


    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId);

    boolean hasActiveAlunosLinkedToResponsavel(UUID parentId);
}
