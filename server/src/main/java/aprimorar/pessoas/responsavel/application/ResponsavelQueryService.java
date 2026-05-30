package aprimorar.pessoas.responsavel.application;

import aprimorar.pessoas.responsavel.ResponsavelQueryApi;
import aprimorar.pessoas.responsavel.web.dto.ResponsaveisListDTO;
import aprimorar.pessoas.responsavel.web.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.responsavel.domain.Responsavel;
import aprimorar.pessoas.responsavel.infrastructure.persistence.ResponsavelRepository;
import aprimorar.pessoas.responsavel.infrastructure.persistence.ResponsavelSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

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

@Service
public class ResponsavelQueryService implements ResponsavelQueryApi {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelQueryService.class);

    private final ResponsavelRepository responsavelRepo;
    private final ResponsavelMapper responsavelMapper;

    public ResponsavelQueryService(ResponsavelRepository responsavelRepo, ResponsavelMapper responsavelMapper) {
        this.responsavelRepo = responsavelRepo;
        this.responsavelMapper = responsavelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean includeArchived) {
        Specification<Responsavel> spec = ResponsavelSpecifications.isNotGhost();

        if (Boolean.FALSE.equals(includeArchived)) {
            spec = spec.and(ResponsavelSpecifications.isNotArchived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ResponsavelSpecifications.searchContainsIgnoreCase(search.trim()));
        }
        Page<Responsavel> responsaveisPage = responsavelRepo.findAll(spec, pageable);
        Page<ResponsavelResponseDTO> responsaveisDtoPage = responsaveisPage.map(responsavelMapper::toResponseDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", responsaveisPage.getTotalElements());
        return new PageDTO<>(responsaveisDtoPage);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponsaveisListDTO> listResponsaveis() {
        List<Responsavel> list = responsavelRepo.findByActiveTrueOrderByNameAsc();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(responsavel -> new ResponsaveisListDTO(responsavel.getId(), responsavel.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponsavelResponseDTO findResponsavelById(UUID responsavelId) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);
        log.info("Responsável {} consultado com sucesso.", responsavel.getName().toUpperCase());
        return responsavelMapper.toResponseDto(responsavel);
    }

    private Responsavel findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo
            .findById(responsavelId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Responsável não encontrado no banco de dados"));
    }
}
