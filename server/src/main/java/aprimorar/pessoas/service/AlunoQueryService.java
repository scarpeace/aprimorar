package aprimorar.pessoas.service;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.AlunoResponseDTO;
import aprimorar.pessoas.dto.AlunosKpisDTO;
import aprimorar.pessoas.dto.AlunosListDTO;
import aprimorar.pessoas.dto.AlunosResponseDTO;
import aprimorar.pessoas.events.AlunoQueryApi;
import aprimorar.pessoas.mappers.AlunoMapper;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.specifications.AlunoSpecifications;
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
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");


    private final AlunoRepository alunoRepo;
    private final AlunoMapper alunoMapper;

    private final Clock clock;

    public AlunoQueryService(AlunoRepository studentRepo, AlunoMapper studentMapper, Clock clock) {
        this.alunoRepo = studentRepo;
        this.alunoMapper = studentMapper;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public AlunosResponseDTO getAlunos(Pageable pageable, String search, Boolean includeArquived) {
        Specification<Aluno> spec = AlunoSpecifications.isNotAdmin();

        if (Boolean.FALSE.equals(includeArquived)) {
            spec = spec.and(AlunoSpecifications.isNotArchived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(AlunoSpecifications.searchContainsIgnoreCase(search.trim()));
        }

        Page<Aluno> studentPage = alunoRepo.findAll(spec, pageable);
        var totalActiveStudents = alunoRepo.countByActiveTrueAndIdNot(GHOST_STUDENT_ID);

        AlunosResponseDTO alunosResponseDTO = new AlunosResponseDTO(
            totalActiveStudents,
            new PageDTO<>(studentPage.map(student -> alunoMapper.toResponseDto(student, clock)))
        );

        log.info("Consulta de alunos finalizada, {} registros encontrados.", studentPage.getTotalElements());
        return alunosResponseDTO;
    }

    @Transactional(readOnly = true)
    public AlunosKpisDTO getAlunosKpis() {
        long totalAlunos = alunoRepo.countByIdNot(GHOST_STUDENT_ID);
        //TODO: ese andIdNot funciona mas é terrível
        long totalAlunosAtivos = alunoRepo.countByActiveTrueAndIdNot(GHOST_STUDENT_ID);
        return new AlunosKpisDTO(totalAlunos, totalAlunosAtivos);
    }

    @Transactional(readOnly = true)
    public List<AlunosListDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        List<AlunosListDTO> alunos = alunoRepo
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
        return alunoMapper.toResponseDto(student, clock);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlunoResponseDTO> getAlunosByResponsavelId(UUID parentId) {
        List<Aluno> students = alunoRepo.findAllByParentId(parentId);
        List<AlunoResponseDTO> alunos = students
            .stream()
            .map(student -> alunoMapper.toResponseDto(student, clock))
            .toList();

        log.info("Consulta de alunos por responsavel finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasActiveAlunosLinkedToResponsavel(UUID parentId) {
        return alunoRepo.existsByParentIdAndActiveTrue(parentId);
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return alunoRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }
}
