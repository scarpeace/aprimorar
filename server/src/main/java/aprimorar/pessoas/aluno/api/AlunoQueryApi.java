package aprimorar.pessoas.aluno.api;

import aprimorar.pessoas.aluno.api.dto.AlunosListDTO;
import aprimorar.pessoas.aluno.api.dto.AlunosResponseDTO;
import aprimorar.pessoas.AlunoResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AlunoQueryApi {


    AlunosResponseDTO getAlunos(Pageable pageable, String search, Boolean archived);
    List<AlunosListDTO> listAlunos();
    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId);

    boolean hasAlunosLinkedToResponsavel(UUID parentId);
    boolean hasActiveAlunosLinkedToResponsavel(UUID parentId);
}
