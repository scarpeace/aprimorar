package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.aluno.api.dto.AlunoCountSummaryDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface AlunoManagementService {

    AlunoResponseDTO createStudent(AlunoRequestDTO dto);

    PageDTO<AlunoResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);

    AlunoCountSummaryDTO getSummary();

    List<AlunoOptionsDTO> getStudentOptions();

    List<AlunoResponseDTO> getStudentsByParent(UUID parentId);

    AlunoResponseDTO findById(UUID studentId);

    AlunoResponseDTO updateStudent(AlunoRequestDTO dto, UUID id);

    void archiveStudent(UUID studentId);

    void unarchiveStudent(UUID studentId);

    void deleteStudent(UUID studentId);
}
