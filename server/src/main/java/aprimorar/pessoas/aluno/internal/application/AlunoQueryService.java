package aprimorar.pessoas.aluno.internal.application;

import aprimorar.pessoas.aluno.api.AlunoQueryApi;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.api.dto.AlunosListDTO;
import aprimorar.pessoas.aluno.api.dto.AlunosResponseDTO;
import aprimorar.pessoas.aluno.internal.domain.Aluno;
import aprimorar.pessoas.aluno.internal.infrastructure.persistence.AlunoRepository;
import aprimorar.pessoas.aluno.internal.infrastructure.persistence.AlunoSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoQueryService implements AlunoQueryApi {

    private static final Logger log = LoggerFactory.getLogger(AlunoQueryService.class);

    private final AlunoRepository studentRepo;
    private final AlunoMapper studentMapper;

    private final Clock clock;

    public AlunoQueryService(AlunoRepository studentRepo, AlunoMapper studentMapper, Clock clock) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    @Override
    public AlunosResponseDTO getAlunos(Pageable pageable, String search, Boolean archived) {
        var totalActiveStudents = studentRepo.countByActiveTrue();
        Specification<Aluno> spec = AlunoSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(AlunoSpecifications.isArchived());
        }
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(AlunoSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Aluno> studentPage = studentRepo.findAll(spec, pageable);
        AlunosResponseDTO alunosResponseDTO = new AlunosResponseDTO(
            totalActiveStudents,
            new PageDTO<>(studentPage.map(student -> studentMapper.toResponseDto(student, clock)))
        );

        log.info("Consulta de alunos finalizada, {} registros encontrados.", studentPage.getTotalElements());
        return alunosResponseDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlunosListDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        List<AlunosListDTO> alunos = studentRepo
            .findAll(AlunoSpecifications.isNotArchived(), sort)
            .stream()
            .map(e -> new AlunosListDTO(e.getId(), e.getName()))
            .toList();

        log.info("Consulta de opcoes de alunos finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    @Override
    public AlunoResponseDTO findAlunoById(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return studentMapper.toResponseDto(student, clock);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId) {
        List<Aluno> students = studentRepo.findAllByParentId(parentId);
        List<AlunoResponseDTO> alunos = students
            .stream()
            .map(student -> studentMapper.toResponseDto(student, clock))
            .toList();

        log.info("Consulta de alunos por responsavel finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasAlunosLinkedToResponsavel(UUID parentId) {
        return studentRepo.existsByParentId(parentId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasActiveAlunosLinkedToResponsavel(UUID parentId) {
        return studentRepo.existsByParentIdAndActiveTrue(parentId);
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return studentRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }
}
