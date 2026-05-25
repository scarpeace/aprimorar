package aprimorar.pessoas.aluno.api;

import aprimorar.pessoas.aluno.api.dto.AlunosListResponseDTO;
import aprimorar.pessoas.aluno.api.dto.AlunosResponseDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AlunoQueryApi {

    AlunosResponseDTO getAlunos(Pageable pageable, String search, Boolean archived);
    List<AlunosListResponseDTO> listAlunos();
    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId);

    boolean hasStudentsLinkedToParent(UUID parentId);
    boolean hasActiveStudentsLinkedToParent(UUID parentId);
}
