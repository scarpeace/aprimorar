package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.aluno.api.AlunoQueryApi;
import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.internal.repository.AlunoRepository;
import aprimorar.pessoas.aluno.internal.repository.AlunoSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AlunoQueryService implements AlunoQueryApi {

    // private static final Logger log = LoggerFactory.getLogger(AlunoQueryService.class);

    private final AlunoRepository studentRepo;
    private final AlunoMapper studentMapper;

    private final Clock clock;

    public AlunoQueryService(
        AlunoRepository studentRepo,
        AlunoMapper studentMapper,
        Clock clock
    ) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.clock = clock;
    }

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public PageDTO<AlunoResponseDTO> getAlunos(Pageable pageable, String search, Boolean archived) {
        Specification<Aluno> spec = AlunoSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(AlunoSpecifications.isArchived());
        }
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(AlunoSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Aluno> studentPage = studentRepo.findAll(spec, pageable);
        Page<AlunoResponseDTO> studentsDtoPage = studentPage.map(student -> studentMapper.toResponseDto(student, clock));

        return new PageDTO<>(studentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<AlunoOptionsDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return studentRepo
            .findAll(AlunoSpecifications.isNotArchived(), sort)
            .stream()
            .map(e -> new AlunoOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

   @Transactional(readOnly = true)
    public List<AlunoResponseDTO> getAlunosPorResponsavel(UUID parentId) {
        List<Aluno> students = studentRepo.findAllByParentId(parentId);
        return students.stream().map(student -> studentMapper.toResponseDto(student, clock)).toList();
    }

    @Transactional(readOnly = true)
    public boolean hasStudentsLinkedToParent(UUID parentId) {
        return studentRepo.existsByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveStudentsLinkedToParent(UUID parentId) {
        return studentRepo.existsByParentIdAndActiveTrue(parentId);
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO findAlunoById(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        return studentMapper.toResponseDto(student, clock);
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return studentRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }
}
