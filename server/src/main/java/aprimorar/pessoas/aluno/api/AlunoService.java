package aprimorar.pessoas.aluno.api;

import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import aprimorar.shared.PageDTO;

public interface AlunoService {

    AlunoResponseDTO createAluno(AlunoRequestDTO alunoDTO);
    PageDTO<AlunoResponseDTO> getAlunos(Pageable pageable, String search, Boolean archived);
    List<AlunoOptionsDTO> listAlunos();
    AlunoResponseDTO findByAlunoId(UUID studentId);
    List<AlunoResponseDTO> getAlunosPorResponsavel(UUID parentId);
    AlunoResponseDTO updateAluno(UUID studentId, AlunoRequestDTO alunoDTO);
    void archiveAluno(UUID studentId);
    void unarchiveAluno(UUID studentId);
    void deleteAluno(UUID studentId);

    boolean hasStudentsLinkedToParent(UUID parentId);
    boolean hasActiveStudentsLinkedToParent(UUID parentId);
}
