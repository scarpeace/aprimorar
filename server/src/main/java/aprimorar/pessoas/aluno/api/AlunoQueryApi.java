package aprimorar.pessoas.aluno.api;

import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import aprimorar.shared.PageDTO;

public interface AlunoQueryApi {

    PageDTO<AlunoResponseDTO> getAlunos(Pageable pageable, String search, Boolean archived);
    List<AlunoOptionsDTO> listAlunos();
    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosPorResponsavel(UUID parentId);

    boolean hasStudentsLinkedToParent(UUID parentId);
    boolean hasActiveStudentsLinkedToParent(UUID parentId);
}
