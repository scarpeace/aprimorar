package aprimorar.atendimentos.application;

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

import aprimorar.atendimentos.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.dto.AlunoAtendimentosKpis;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.dto.ColaboradorAtendimentosKpis;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.infrastructure.persistence.AtendimentoRepository;
import aprimorar.atendimentos.infrastructure.persistence.AtendimentoSpecifications;
import aprimorar.pessoas.events.AlunoQueryApi;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

@Service
public class AtendimentoQueryService {

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
    public AtendimentoResponseDTO findAtendimentoById(UUID id) {
        Atendimento atendimento = atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento nao encontrado"));
        log.info("Atendimento {} consultado com sucesso.", atendimento.getTitle().toUpperCase());
        return atendimentoMapper.convertToDto(atendimento);
    }

    @Transactional(readOnly = true)
    public AtendimentosColaboradorResponseDTO getAtendimentosByColaborador(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    ) {
        Boolean paidFilter = Boolean.TRUE.equals(hidePaid) ? Boolean.FALSE : null;

        Specification<Atendimento> spec = AtendimentoSpecifications.withEmployeeId(employeeId)
            .and(AtendimentoSpecifications.withStartDateAfter(startDate))
            .and(AtendimentoSpecifications.withEndDateBefore(endDate))
            .and(AtendimentoSpecifications.withEmployeePaid(paidFilter));

        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);
        Page<AtendimentoResponseDTO> dtoPage = atendimentoPage.map(atendimentoMapper::convertToDto);

        long totalAtendimentos = atendimentoRepo.countByEmployeeIdInPeriod(employeeId, startDate, endDate);
        BigDecimal totalPaid = atendimentoRepo.sumPaidByEmployeeIdInPeriod(employeeId, startDate, endDate);
        BigDecimal totalUnpaid = atendimentoRepo.sumUnpaidByEmployeeIdInPeriod(employeeId, startDate, endDate);

        log.info("Consulta de atendimentos do colaborador finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());

        return new AtendimentosColaboradorResponseDTO(
            new PageDTO<>(dtoPage),
            new ColaboradorAtendimentosKpis(totalAtendimentos, totalPaid, totalUnpaid)
        );
    }

    @Transactional(readOnly = true)
    public AtendimentosAlunoResponseDTO getAtendimentosByAluno(
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

        return new AtendimentosAlunoResponseDTO(new PageDTO<>(dtoPage), new AlunoAtendimentosKpis(totalAtendimentos, totalCharged, totalPending));
    }

}
