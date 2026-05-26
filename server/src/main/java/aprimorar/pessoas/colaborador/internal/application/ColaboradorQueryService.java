package aprimorar.pessoas.colaborador.internal.application;

import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.pessoas.colaborador.api.ColaboradorDutyEnum;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresListDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresResponseDTO;
import aprimorar.pessoas.colaborador.internal.domain.Colaborador;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.pessoas.colaborador.internal.infrastructure.persistence.ColaboradorRepository;
import aprimorar.pessoas.colaborador.internal.infrastructure.persistence.ColaboradorSpecifications;
import aprimorar.shared.exception.BusinessException;


@Service
public class ColaboradorQueryService implements ColaboradorQueryApi {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorQueryService.class);

    private final ColaboradorRepository colaboradorRepo;
    private final ColaboradorMapper colaboradorMapper;

    public ColaboradorQueryService(ColaboradorRepository colaboradorRepo, ColaboradorMapper colaboradorMapper) {
        this.colaboradorRepo = colaboradorRepo;
        this.colaboradorMapper = colaboradorMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ColaboradoresResponseDTO getColaboradores(Pageable pageable, String busca, Boolean arquivado) {
        long colaboradoresAtivos = colaboradorRepo.countByActiveTrue();
        Specification<Colaborador> spec = ColaboradorSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(arquivado)) {
            spec = spec.and(ColaboradorSpecifications.isArchived());
        }

        if (busca != null && !busca.trim().isEmpty()) {
            spec = spec.and(ColaboradorSpecifications.searchContainsIgnoreCase(busca.trim()));
        }

        Page<Colaborador> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);

        ColaboradoresResponseDTO colaboradoresDtoPage = new ColaboradoresResponseDTO(
            colaboradoresAtivos,
            colaboradoresPage.map(colaborador -> colaboradorMapper.toDto(colaborador))
        );

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresDtoPage;
    }

    @Transactional(readOnly = true)
    @Override
    public ColaboradorResponseDTO findColaboradorById(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getName().toUpperCase());
        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ColaboradoresListDTO> listColaboradores() {
        return colaboradorRepo.findAllByDutyNotAndActiveTrueOrderByNameAsc(ColaboradorDutyEnum.SYSTEM)
            .stream()
            .map(e -> new ColaboradoresListDTO(e.getId(), e.getName()))
            .toList();
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado no banco de dados"));
    }
}
