package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.mappers.AtendimentoMapper;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.pessoas.dto.AlunoResponseDTO;
import aprimorar.pessoas.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.events.AlunoQueryApi;
import aprimorar.pessoas.events.ColaboradorQueryApi;
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
public class AtendimentoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoMutationService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AtendimentoMapper atendimentoMapper;
    private final AlunoQueryApi alunoQueryApi;
    private final ColaboradorQueryApi colaboradorQueryApi;
    private final Clock clock;

    public AtendimentoMutationService(
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
        AlunoResponseDTO student = alunoQueryApi.findAlunoById(dto.studentId());
        ColaboradorResponseDTO colaborador = colaboradorQueryApi.findColaboradorById(dto.employeeId());
        Atendimento atendimento = atendimentoMapper.toEntity(dto, student.name(), colaborador.name(), clock.instant());

        validateParticipantAvailability(student, colaborador, dto.startDate(), dto.duration(), null);

        Atendimento saved = atendimentoRepo.save(atendimento);
        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitle().toUpperCase());
        return atendimentoMapper.convertToDto(saved);
    }

    @Transactional
    public AtendimentoResponseDTO updateAtendimento(UUID id, AtendimentoRequestDTO dto) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        AlunoResponseDTO student = alunoQueryApi.findAlunoById(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorQueryApi.findColaboradorById(dto.employeeId());

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

        if (atendimento.getEmployeePaymentDate() == null || atendimento.getStudentChargeDate() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento não pode ser excluído com cobranças ou pagamentos pendentes.");
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

    private Atendimento findAtendimentoOrThrow(UUID id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
    }

    private void validateParticipantAvailability(
        AlunoResponseDTO student,
        ColaboradorResponseDTO colaborador,
        Instant startDate,
        Double duration,
        Atendimento atendimento
    ) {
        Instant endDate = Atendimento.calculateEndDate(startDate, duration);

        if (!student.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter estudantes arquivados");
        }

        if (!colaborador.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter colaboradores arquivados");
        }

        UUID atendimentoId = atendimento != null ? atendimento.getId() : null;

        if (atendimentoRepo.studentHasConflictingAtendimento(student.id(), startDate, endDate, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O estudante informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.employeeHasConflictingAtendimento(colaborador.id(), startDate, endDate, atendimentoId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
