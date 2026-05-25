package aprimorar.atendimentos.internal;

import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.pessoas.aluno.api.AlunoQueryApi;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.internal.Colaborador;
import aprimorar.shared.exception.BusinessException;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AtendimentoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoMutationService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID GHOST_COLABORADOR_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");

    private final AtendimentoRepository atendimentoRepo;
    private final AtendimentoMapper atendimentoMapper;
    private final AlunoQueryApi alunoQueryApi;
    private final ColaboradorQueryApi colaboradorQueryApi;
    private final Clock clock;

    AtendimentoMutationService(
        AtendimentoRepository atendimentoRepo,
        AtendimentoMapper atendimentoMapper,
        AlunoQueryApi alunoQueryApi,
        ColaboradorQueryApi colaboradorQueryApi,
        Clock clock
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.atendimentoMapper = atendimentoMapper;
        this.alunoQueryApi = alunoQueryApi;
        this.colaboradorQueryApi = colaboradorQueryApi;
        this.clock = clock;
    }

    @Transactional
    public AtendimentoResponseDTO createAtendimento(AtendimentoRequestDTO dto) {

        Colaborador colaborador = colaboradorQueryApi.findColaboradorById(dto.employeeId());
        Aluno aluno = alunoQueryApi.findAlunoById(dto.studentId());
        Atendimento atendimento = atendimentoMapper.toEntity(dto, student.name(), colaborador.getName(), clock.instant());

        validateParticipantAvailability(student, colaborador, dto.startDate(), dto.duration(), null);

        Atendimento saved = atendimentoRepo.save(atendimento);
        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitle().toUpperCase());
        return atendimentoMapper.convertToDto(saved);
    }

    @Transactional
    public AtendimentoResponseDTO updateAtendimento(UUID id, AtendimentoRequestDTO dto) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        AlunoResponseDTO student = alunoQueryApi.findAlunoById(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorService.findColaboradorById(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), atendimento);

        atendimento.update(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            student.id(),
            student.name(),
            employee.id(),
            employee.name(),
            Instant.now(clock)
        );
        log.info("Atendimento {} atualizado com sucesso.", atendimento.getTitle().toUpperCase());
        return atendimentoMapper.convertToDto(atendimento);
    }

    @Transactional
    public void deleteAtendimento(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);

        if (atendimento.getEmployeePaymentDate() == null && atendimento.getStudentChargeDate() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento não pode ser e com pagamentos pendentes");
        }

        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getTitle().toUpperCase());
    }

    @Transactional
    public AtendimentoResponseDTO toggleStudentCharge(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.toggleStudentCharge(Instant.now(clock));
        log.info("Status da cobranca do aluno no atendimento {} atualizado.", atendimento.getTitle());
        return atendimentoMapper.convertToDto(atendimento);
    }

    @Transactional
    public AtendimentoResponseDTO toggleEmployeePayment(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.toggleEmployeePayment(Instant.now(clock));
        log.info("Status do pagamento do colaborador no atendimento {} atualizado.", atendimento.getTitle());
        return atendimentoMapper.convertToDto(atendimento);
    }

    @Transactional
    public void reassignStudentAtendimentosToGhost(UUID studentId) {
        if (atendimentoRepo.existsByStudentIdAndStudentChargeDateIsNull(studentId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O aluno possui pagamentos pendentes e nao pode ser excluido.");
        }
        atendimentoRepo.reassignStudentAtendimentosToGhost(studentId, GHOST_STUDENT_ID);
    }

    @Transactional
    public void onEmployeeDeleted(UUID colaboradorId) {
        if (atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(colaboradorId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador possui pagamentos pendentes e nao pode ser excluido.");
        }
        atendimentoRepo.reassignEmployeeAtendimentosToGhost(colaboradorId, GHOST_COLABORADOR_ID);
    }

    private Atendimento findAtendimentoOrThrow(UUID id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
    }

    private void validateParticipantAvailability(
        AlunoResponseDTO student,
        Colaborador colaborador,
        Instant startDate,
        Double duration,
        Atendimento atendimento
    ) {
        Instant endDate = Atendimento.calculateEndDate(startDate, duration);

        if (!student.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter estudantes arquivados");
        }

        if (!colaborador.getActive()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter colaboradores arquivados");
        }

        UUID atendimentoId = atendimento != null ? atendimento.getId() : null;

        if (atendimentoRepo.studentHasConflictingAtendimento(student.id(), startDate, endDate, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O estudante informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.employeeHasConflictingAtendimento(colaborador.getId(), startDate, endDate, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
