package aprimorar.atendimentos.internal.application;

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
import java.util.List;

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

        long totalAtendimentos = atendimentoRepo.countFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalPaid = atendimentoRepo.sumPaidFilteredByEmployeeId(employeeId, startDate, endDate);
        BigDecimal totalUnpaid = atendimentoRepo.sumUnpaidFilteredByEmployeeId(employeeId, startDate, endDate);

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

        long totalAtendimentos = atendimentoRepo.countFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalCharged = atendimentoRepo.sumChargedFilteredByStudentId(studentId, startDate, endDate);
        BigDecimal totalPending = atendimentoRepo.sumPendingFilteredByStudentId(studentId, startDate, endDate);

        return new AtendimentosAlunoResponseDTO(new PageDTO<>(dtoPage), new AlunoSummaryDTO(totalAtendimentos, totalCharged, totalPending));
    }

    //TODO: To querendo mover isso para getAtendimentos
    @Transactional(readOnly = true)
    @Override
    public AtendimentoFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate) {
        BigDecimal totalStudentCharged = atendimentoRepo.sumChargedFiltered(startDate, endDate);
        BigDecimal totalStudentPending = atendimentoRepo.sumPendingFiltered(startDate, endDate);
        BigDecimal totalEmployeePaid = atendimentoRepo.sumPaidFiltered(startDate, endDate);
        BigDecimal totalEmployeePending = atendimentoRepo.sumUnpaidFiltered(startDate, endDate);

        return new AtendimentoFinanceSummaryDTO(
            totalStudentCharged,
            totalStudentPending,
            totalEmployeePaid,
            totalEmployeePending
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorFinanceiroResumoDTO> getColaboradoresFinanceiroByPeriod(
        List<UUID> employeeIds,
        Instant startDate,
        Instant endDate
    ) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return List.of();
        }

        return atendimentoRepo.findColaboradoresFinanceiroByPeriodAndIds(employeeIds, startDate, endDate)
            .stream()
            .map(projection -> new ColaboradorFinanceiroResumoDTO(
                projection.getEmployeeId(),
                projection.getTotalAtendimentos(),
                projection.getTotalPaid(),
                projection.getTotalPending()
            ))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId) {
        return atendimentoRepo.countDistinctStudentsInPeriodExcludingAluno(startDate, endDate, excludedStudentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAtendimentosInPeriod(Instant startDate, Instant endDate) {
        return atendimentoRepo.countByStartDateGreaterThanEqualAndStartDateLessThan(startDate, endDate);
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
