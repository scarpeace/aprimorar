package aprimorar.pessoas.aluno.api;

import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import aprimorar.shared.PageDTO;

public interface AlunoService {

    AlunoResponseDTO findById(UUID studentId);

    PageDTO<AlunoResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);
}
