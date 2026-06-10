package aprimorar.pessoas.events;

import java.util.List;
import java.util.UUID;

import aprimorar.pessoas.dto.AlunoResponseDTO;

public interface AlunoQueryApi {


    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId);

    boolean hasActiveAlunosLinkedToResponsavel(UUID parentId);
}
