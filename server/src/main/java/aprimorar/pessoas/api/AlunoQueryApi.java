package aprimorar.pessoas.api;

import aprimorar.pessoas.dto.AlunoFiltroRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlunoQueryApi {
    Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable);
    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID responsavelId);
    boolean hasActiveAlunosLinkedToResponsavel(UUID responsavelId);
}
