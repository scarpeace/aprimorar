package aprimorar.pessoas.service;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.AlunoFiltroRequest;
import aprimorar.pessoas.api.AlunoResponseDTO;
import aprimorar.pessoas.dto.AlunosKpisDTO;
import aprimorar.pessoas.dto.AlunosListDTO;
import aprimorar.pessoas.api.AlunoQueryApi;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.specifications.AlunoSpecifications;
import aprimorar.shared.exception.BusinessException;
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
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");


    private final AlunoRepository alunoRepo;

    public AlunoQueryService(AlunoRepository studentRepo) {
        this.alunoRepo = studentRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<Aluno> spec = AlunoSpecifications.comFiltros(filtro);
        Page<Aluno> alunosPage = alunoRepo.findAll(spec, pageable);
        Page<AlunoResponseDTO> alunosDtoPage = alunosPage.map(AlunoResponseDTO::toDto);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", alunosPage.getTotalElements());
        return alunosDtoPage;
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
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        List<AlunosListDTO> alunos = alunoRepo
            .findAll(AlunoSpecifications.isNotArchived(), sort)
            .stream()
            .map(e -> new AlunosListDTO(e.getId(), e.getNome()))
            .toList();

        log.info("Consulta de opcoes de alunos finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    @Override
    public AlunoResponseDTO findAlunoById(UUID studentId) {
        Aluno student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(student);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlunoResponseDTO> getAlunosByResponsavelId(UUID responsavelId) {
        List<Aluno> students = alunoRepo.findAllByResponsavelId(responsavelId);
        List<AlunoResponseDTO> alunos = students
            .stream()
            .map(AlunoResponseDTO::toDto)
            .toList();

        log.info("Consulta de alunos por responsavel finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasActiveAlunosLinkedToResponsavel(UUID responsavelId) {
        return alunoRepo.existsByResponsavelIdAndActiveTrue(responsavelId);
    }

    /* ----- Helper Methods ----- */
    private Aluno findStudentOrThrow(UUID studentId) {
        return alunoRepo
            .findById(studentId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }
}
