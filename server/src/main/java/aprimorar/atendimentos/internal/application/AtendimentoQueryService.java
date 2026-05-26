package aprimorar.atendimentos.internal.application;

import aprimorar.atendimentos.api.dto.AlunoFinanceiroResumoDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.api.AtendimentosQueryApi;
import aprimorar.atendimentos.api.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.api.dto.AlunoSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorFinanceiroResumoDTO;
import aprimorar.atendimentos.api.dto.ColaboradorSummaryDTO;
import aprimorar.atendimentos.internal.domain.Atendimento;
import aprimorar.atendimentos.internal.infrastructure.persistence.AtendimentoRepository;
import aprimorar.atendimentos.internal.infrastructure.persistence.AtendimentoSpecifications;
import aprimorar.pessoas.aluno.api.AlunoQueryApi;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

@Service
public class AtendimentoQueryService implements AtendimentosQueryApi {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoQueryService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AtendimentoMapper atendimentoMapper;
    private final AlunoQueryApi alunoQueryApi;

    public AtendimentoQueryService(
        AtendimentoRepository atendimentoRepo,
        AtendimentoMapper atendimentoMapper,
        AlunoQueryApi alunoQueryApi
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.atendimentoMapper = atendimentoMapper;
        this.alunoQueryApi = alunoQueryApi;
    }

    @Transactional(readOnly = true)
    @Override
    public PageDTO<AtendimentoResponseDTO> getAtendimentos(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    ) {
        Boolean chargedFilter = Boolean.TRUE.equals(hideCharged) ? Boolean.FALSE : null;
        Boolean paidFilter = Boolean.TRUE.equals(hidePaid) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.searchContains(search)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withStudentCharged(chargedFilter))
            .and(AtendimentoSpecifications.withEmployeePaid(paidFilter));

        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentoPage.map(atendimentoMapper::convertToDto);

        log.info("Consulta de atendimentos finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());
        return new PageDTO<>(dtoPage);
    }

    @Transactional(readOnly = true)
    @Override
    public AtendimentoResponseDTO findAtendimentoById(UUID id) {
        Atendimento atendimento = atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
        log.info("Atendimento {} consultado com sucesso.", atendimento.getTitle().toUpperCase());
        return atendimentoMapper.convertToDto(atendimento);
    }

    @Transactional(readOnly = true)
    @Override
    public AtendimentoFinanceSummaryDTO getIndicadoresAtendimentos(Instant startDate, Instant endDate) {
        BigDecimal totalStudentCharged = atendimentoRepo.sumTotalChargedInPeriod(startDate, endDate);
        BigDecimal totalStudentPending = atendimentoRepo.sumTotalUnchargedInPeriod(startDate, endDate);
        BigDecimal totalEmployeePaid = atendimentoRepo.sumTotalPaidInPeriod(startDate, endDate);
        BigDecimal totalEmployeePending = atendimentoRepo.sumTotalUnpaidInPeriod(startDate, endDate);

        return new AtendimentoFinanceSummaryDTO(
            totalStudentCharged,
            totalStudentPending,
            totalEmployeePaid,
            totalEmployeePending
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AtendimentosColaboradorResponseDTO getAtendimentosByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    ) {

        Specification<Atendimento> spec = AtendimentoSpecifications.withEmployeeId(employeeId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withEmployeePaid(hidePaid));

        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentoPage.map(atendimentoMapper::convertToDto);

        long totalAtendimentos = atendimentoRepo.countByEmployeeIdInPeriod(employeeId, startDate, endDate);
        BigDecimal totalPaid = atendimentoRepo.sumPaidByEmployeeIdInPeriod(employeeId, startDate, endDate);
        BigDecimal totalUnpaid = atendimentoRepo.sumUnpaidByEmployeeIdInPeriod(employeeId, startDate, endDate);

        log.info("Consulta de atendimentos do colaborador finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());

        return new AtendimentosColaboradorResponseDTO(
            new PageDTO<>(dtoPage),
            new ColaboradorSummaryDTO(totalAtendimentos, totalPaid, totalUnpaid)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AtendimentosAlunoResponseDTO getAtendimentosByStudentId(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    ) {
        alunoQueryApi.findAlunoById(studentId);

        Specification<Atendimento> spec = AtendimentoSpecifications.withStudentId(studentId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withStudentCharged(charged));

        Page<Atendimento> atendimentos = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentos.map(atendimentoMapper::convertToDto);

        long totalAtendimentos = atendimentoRepo.countByStudentIdInPeriod(studentId, startDate, endDate);
        BigDecimal totalCharged = atendimentoRepo.sumChargedByStudentInPeriod(studentId, startDate, endDate);
        BigDecimal totalPending = atendimentoRepo.sumPendingByStudentInPeriod(studentId, startDate, endDate);

        return new AtendimentosAlunoResponseDTO(new PageDTO<>(dtoPage), new AlunoSummaryDTO(totalAtendimentos, totalCharged, totalPending));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<ColaboradorFinanceiroResumoDTO> getOverviewFinanceiroColaboradores(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    ) {
        Page<ColaboradorFinanceiroResumoDTO> resultado = atendimentoRepo
            .getOverviewFinanceiroColaboradores(pageable, startDate, endDate)
            .map(item -> new ColaboradorFinanceiroResumoDTO(
                item.getEmployeeId(),
                item.getTotalAtendimentos(),
                item.getTotalPaid(),
                item.getTotalPending()
            ));

        return new PageDTO<>(resultado);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<AlunoFinanceiroResumoDTO> getOverviewFinanceiroAlunos(
        Pageable pageable,
        Instant startDate,
        Instant endDate
    ) {
        Page<AlunoFinanceiroResumoDTO> resultado = atendimentoRepo
            .getOverviewFinanceiroAlunos(pageable, startDate, endDate)
            .map(item -> new AlunoFinanceiroResumoDTO(
                item.getStudentId(),
                item.getTotalAtendimentos(),
                item.getTotalCharged(),
                item.getTotalPending()
            ));

        return new PageDTO<>(resultado);
    }

    @Transactional(readOnly = true)
    public long countAllStudentsInPeriod(Instant startDate, Instant endDate) {
        return atendimentoRepo.countStudentsInPeriod(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countAllActiveStudentsInPeriod(Instant startDate, Instant endDate) {
        return atendimentoRepo.countActiveStudentsInPeriod(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countAtendimentosInPeriod(Instant startDate, Instant endDate) {
        return atendimentoRepo.countAtendimentosInPeriod(startDate, endDate);
    }

    @Override
    public boolean colaboradorHasPendingPayment(UUID colaboradorId) {
        return atendimentoRepo.existsByEmployeeIdAndEmployeePaymentDateIsNull(colaboradorId);
    }

    @Override
    public boolean alunoHasPendingCharges(UUID alunoId) {
        return atendimentoRepo.existsByStudentIdAndStudentChargeDateIsNull(alunoId);
    }
}
