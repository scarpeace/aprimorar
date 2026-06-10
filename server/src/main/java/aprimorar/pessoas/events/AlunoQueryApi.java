package aprimorar.pessoas.events;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import aprimorar.pessoas.dto.AlunoFiltroRequest;
import aprimorar.pessoas.dto.AlunoResponseDTO;

public interface AlunoQueryApi {


    Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable);
    AlunoResponseDTO findAlunoById(UUID studentId);
    List<AlunoResponseDTO> getAlunosByResponsavelId(UUID responsavelId);

    boolean hasActiveAlunosLinkedToResponsavel(UUID responsavelId);
}
