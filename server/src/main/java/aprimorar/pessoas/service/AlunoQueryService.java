package aprimorar.pessoas.service;

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.aluno.AlunoFiltroRequest;
import aprimorar.pessoas.dto.aluno.AlunoResponseDTO;
import aprimorar.pessoas.dto.aluno.AlunosKpisDTO;
import aprimorar.pessoas.dto.aluno.AlunosListDTO;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.specifications.AlunoSpecifications;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoQueryService {

    private static final Logger log = LoggerFactory.getLogger(AlunoQueryService.class);

    private final AlunoRepository alunoRepo;
    private final UUID ghostStudentId;

    public AlunoQueryService(
        AlunoRepository studentRepo,
        @Value("${aprimorar.ghost-student-id}") String ghostStudentId
    ) {
        this.alunoRepo = studentRepo;
        this.ghostStudentId = UUID.fromString(ghostStudentId);
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<Aluno> spec = AlunoSpecifications.comFiltros(filtro, ghostStudentId);
        Page<Aluno> alunosPage = alunoRepo.findAll(spec, pageable);
        Page<AlunoResponseDTO> alunosDtoPage = alunosPage.map(AlunoResponseDTO::toDto);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", alunosPage.getTotalElements());
        return alunosDtoPage;
    }

    @Transactional(readOnly = true)
    public AlunosKpisDTO getAlunosKpis() {
        long totalAlunos = alunoRepo.countByIdNot(ghostStudentId);
        long totalAlunosAtivos = alunoRepo.countByActiveTrueAndIdNot(ghostStudentId);
        return new AlunosKpisDTO(totalAlunos, totalAlunosAtivos);
    }

    @Transactional(readOnly = true)
    public List<AlunosListDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        List<AlunosListDTO> alunos = alunoRepo
            .findAll(AlunoSpecifications.isNotArchived().and(AlunoSpecifications.isNotGhost(ghostStudentId)), sort)
            .stream()
            .map(e -> new AlunosListDTO(e.getId(), e.getNome()))
            .toList();

        log.info("Consulta de opcoes de alunos finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO findAlunoById(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(student);
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> getAlunosByResponsavelId(UUID responsavelId) {
        List<Aluno> students = alunoRepo.findAllByResponsavelId(responsavelId);
        List<AlunoResponseDTO> alunos = students
            .stream()
            .map(AlunoResponseDTO::toDto)
            .toList();

        log.info("Consulta de alunos por responsavel finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    private Aluno findStudentOrThrow(UUID studentId) {
        return alunoRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }


}
