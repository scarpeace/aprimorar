package aprimorar.pessoas.colaborador.internal;

import aprimorar.pessoas.colaborador.api.contract.ColaboradorReadApi;
import aprimorar.pessoas.colaborador.api.contract.DutyEnum;
import aprimorar.pessoas.colaborador.api.contract.ColaboradorPaymentStatusApi;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorCountSummaryDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorOptionsDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.api.exception.ColaboradorBusinessException;
import aprimorar.pessoas.colaborador.api.exception.ColaboradorNotFoundException;
import aprimorar.pessoas.colaborador.api.event.ColaboradorDeletedEvent;
import aprimorar.pessoas.colaborador.internal.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.internal.repository.ColaboradorSpecifications;
import aprimorar.pessoas.shared.address.AddressMapper;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorServiceImpl implements ColaboradorService, ColaboradorReadApi {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorServiceImpl.class);
    private final ColaboradorRepository colaboradorRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final ColaboradorPaymentStatusApi colaboradorPaymentStatusApi;

    public ColaboradorServiceImpl(
        ColaboradorRepository colaboradorRepo,
        ApplicationEventPublisher eventPublisher,
        ColaboradorPaymentStatusApi colaboradorPaymentStatusApi
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.eventPublisher = eventPublisher;
        this.colaboradorPaymentStatusApi = colaboradorPaymentStatusApi;
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        if (colaboradorRepo.existsByCpf(MapperUtils.normalizeCpf(dto.cpf()))) {
            throw new ColaboradorBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(MapperUtils.normalizeEmail(dto.email()))) {
            throw new ColaboradorBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        var colaborador = ColaboradorMapper.toEntity(dto);
        Colaborador savedColaborador = colaboradorRepo.save(colaborador);

        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getName().toUpperCase());
        return ColaboradorMapper.toDto(savedColaborador);
    }

    @Transactional(readOnly = true)
    public PageDTO<ColaboradorResponseDTO> getColaboradores(Pageable pageable, String busca, Boolean arquivado) {
        Specification<Colaborador> spec = ColaboradorSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(arquivado)) {
            spec = spec.and(ColaboradorSpecifications.isArchived());
        }

        if (busca != null && !busca.trim().isEmpty()) {
            spec = spec.and(ColaboradorSpecifications.searchContainsIgnoreCase(busca.trim()));
        }

        Page<Colaborador> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);
        Page<ColaboradorResponseDTO> colaboradoresDtoPage = colaboradoresPage.map(ColaboradorMapper::toDto);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return new PageDTO<>(colaboradoresDtoPage);
    }

    @Transactional(readOnly = true)
    public ColaboradorResponseDTO buscarPorId(UUID colaboradorId) {
        Colaborador colaborador = buscarColaboradorOuFalhar(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getName().toUpperCase());
        return ColaboradorMapper.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public ColaboradorCountSummaryDTO getSummary() {
        long activeEmployees = colaboradorRepo.countByDutyNotAndActiveTrue(DutyEnum.SYSTEM);
        long totalEmployees = colaboradorRepo.countByDutyNot(DutyEnum.SYSTEM);

        return new ColaboradorCountSummaryDTO(activeEmployees, totalEmployees);
    }

    @Transactional(readOnly = true)
    public List<ColaboradorOptionsDTO> listarOpcoes() {
        return colaboradorRepo
            .findAllByDutyNotAndActiveTrueOrderByNameAsc(DutyEnum.SYSTEM)
            .stream()
            .map(e -> new ColaboradorOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {
        if (colaboradorRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), colaboradorId)) {
            throw new ColaboradorBusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
        }

        var colaborador = colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorBusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado."));

        ensureNotSystem(colaborador, "Não é possível modificar o registro de sistema 'Colaborador Removido'.");

        colaborador.update(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.email(),
            dto.duty(),
            AddressMapper.toEntity(dto.address())
        );

        log.info("Colaborador {} atualizado com sucesso.", colaborador.getName().toUpperCase());
        return ColaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        Colaborador colaborador = buscarColaboradorOuFalhar(colaboradorId);
        ensureNotSystem(colaborador, "Não é possível deletar o registro de sistema 'Colaborador Removido'.");

        if (colaboradorPaymentStatusApi.possuiPagamentosPendentes(colaboradorId)) {
            throw new ColaboradorBusinessException(HttpStatus.BAD_REQUEST,"O colaborador possui pagamentos pendentes. Quite os valores antes de excluí-lo.");
        }

        eventPublisher.publishEvent(new ColaboradorDeletedEvent(colaboradorId));
        colaboradorRepo.delete(colaborador);

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getName().toUpperCase()
        );
    }

    @Transactional
    public void archiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = buscarColaboradorOuFalhar(colaboradorId);

        if (colaboradorPaymentStatusApi.possuiPagamentosPendentes(colaboradorId)) {
            throw new ColaboradorBusinessException(HttpStatus.BAD_REQUEST,"O colaborador possui pagamentos pendentes. Quite os valores antes de arquivar.");
        }

        ensureNotSystem(colaborador, "Não é possível arquivar o registro de sistema 'Colaborador Removido'.");
        colaborador.archive();

        log.info("Colaborador {} arquivado com sucesso.", colaborador.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = buscarColaboradorOuFalhar(colaboradorId);
        ensureNotSystem(colaborador, "O registro 'Colaborador Removido' não pode ser desarquivado.");
        colaborador.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", colaborador.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Colaborador buscarColaboradorOuFalhar(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorNotFoundException("Colaborador não encontrado no banco de dados"));
    }

    private void ensureNotSystem(Colaborador employee, String message) {
        if (DutyEnum.SYSTEM.equals(employee.getDuty())) {
            throw new ColaboradorBusinessException(HttpStatus.CONFLICT, message);
        }
    }
}
