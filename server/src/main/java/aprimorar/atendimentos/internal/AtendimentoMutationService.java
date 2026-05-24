package aprimorar.atendimentos.internal;

import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
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
    private final AtendimentoMapper appointmentMapper;
    private final AlunoService studentService;
    private final ColaboradorQueryApi colaboradorService;
    private final Clock clock;

    AtendimentoMutationService(
        AtendimentoRepository atendimentoRepo,
        AtendimentoMapper appointmentMapper,
        AlunoService studentService,
        ColaboradorQueryApi colaboradorService,
        Clock clock
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.appointmentMapper = appointmentMapper;
        this.studentService = studentService;
        this.colaboradorService = colaboradorService;
        this.clock = clock;
    }

    @Transactional
    public AtendimentoResponseDTO createAtendimento(AtendimentoRequestDTO dto) {
        AlunoResponseDTO student = studentService.findByAlunoId(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorService.findColaboradorById(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), null);

        Atendimento appointment = new Atendimento(
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
        Atendimento saved = atendimentoRepo.save(appointment);
        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(saved);
    }

    @Transactional
    public AtendimentoResponseDTO updateAppointment(UUID id, AtendimentoRequestDTO dto) {
        Atendimento appointment = findAppointmentOrThrow(id);
        AlunoResponseDTO student = studentService.findByAlunoId(dto.studentId());
        ColaboradorResponseDTO employee = colaboradorService.findColaboradorById(dto.employeeId());

        validateParticipantAvailability(student, employee, dto.startDate(), dto.duration(), appointment);

        appointment.update(
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
        log.info("Atendimento {} atualizado com sucesso.", appointment.getTitle().toUpperCase());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        Atendimento found = findAppointmentOrThrow(id);
        atendimentoRepo.delete(found);
        log.info("Atendimento {} deletado com sucesso.", found.getTitle().toUpperCase());
    }

    @Transactional
    public AtendimentoResponseDTO toggleStudentCharge(UUID id) {
        Atendimento appointment = findAppointmentOrThrow(id);
        appointment.toggleStudentCharge(Instant.now(clock));
        log.info("Status da cobranca do aluno no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    @Transactional
    public AtendimentoResponseDTO toggleEmployeePayment(UUID id) {
        Atendimento appointment = findAppointmentOrThrow(id);
        appointment.toggleEmployeePayment(Instant.now(clock));
        log.info("Status do pagamento do colaborador no appointment {} atualizado.", appointment.getTitle());
        return appointmentMapper.convertToDto(appointment);
    }

    private Atendimento findAppointmentOrThrow(UUID id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
    }

    private void validateParticipantAvailability(
        AlunoResponseDTO student,
        ColaboradorResponseDTO employee,
        Instant startDate,
        Double duration,
        Atendimento appointment
    ) {
        Instant endDate = Atendimento.calculateEndDate(startDate, duration);

        if (!student.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter estudantes arquivados");
        }

        if (!employee.active()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Atendimento nao pode ter colaboradores arquivados");
        }

        UUID appointmentId = appointment != null ? appointment.getId() : null;

        if (atendimentoRepo.studentHasConflictingAppointment(student.id(), startDate, endDate, appointmentId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O estudante informado ja possui um appointment no intervalo");
        }

        if (atendimentoRepo.employeeHasConflictingAppointment(employee.id(), startDate, endDate, appointmentId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um appointment no intervalo");
        }
    }

    @Transactional
    public void reassignStudentAppointmentsToGhost(UUID studentId) {
        if (atendimentoRepo.existsByStudentIdAndEmployeeChargeDateIsNull(studentId)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O aluno possui pagamentos pendentes e nao pode ser excluido.");
        }
        atendimentoRepo.reassignStudentAppointmentsToGhost(studentId, GHOST_STUDENT_ID);
    }

    @Transactional
    public void onEmployeeDeleted(UUID colaboradorId) {
        if (atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(colaboradorId)) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "O colaborador possui pagamentos pendentes e nao pode ser excluido."
            );
        }
        atendimentoRepo.reassignEmployeeAppointmentsToGhost(colaboradorId, GHOST_COLABORADOR_ID);
    }
}
