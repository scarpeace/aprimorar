package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.dto.ResponsavelOptionsDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.responsavel.internal.exception.ResponsavelNotFoundException;
import aprimorar.pessoas.responsavel.internal.repository.ResponsavelRepository;
import aprimorar.pessoas.responsavel.internal.repository.ResponsavelSpecifications;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ResponsavelQueryService {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelQueryService.class);

    private final ResponsavelRepository responsavelRepo;
    private final ResponsavelMapper responsavelMapper;

    ResponsavelQueryService(ResponsavelRepository responsavelRepo, ResponsavelMapper responsavelMapper) {
        this.responsavelRepo = responsavelRepo;
        this.responsavelMapper = responsavelMapper;
    }

    @Transactional(readOnly = true)
    public PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean archived) {
        Specification<Responsavel> spec = ResponsavelSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(ResponsavelSpecifications.isArchived());
        } else if (Boolean.FALSE.equals(archived)) {
            spec = spec.and(ResponsavelSpecifications.isNotArchived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ResponsavelSpecifications.searchContainsIgnoreCase(search.trim()));
        }
        Page<Responsavel> parentsPage = responsavelRepo.findAll(spec, pageable);
        Page<ResponsavelResponseDTO> parentsDtoPage = parentsPage.map(responsavelMapper::toResponseDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", parentsPage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<ResponsavelOptionsDTO> listResponsaveis() {
        List<Responsavel> list = responsavelRepo.findByActiveTrueOrderByNameAsc();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(parent -> new ResponsavelOptionsDTO(parent.getId(), parent.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findResponsavelById(UUID parentId) {
        Responsavel parent = findResponsavelOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return responsavelMapper.toResponseDto(parent);
    }

    private Responsavel findResponsavelOrThrow(UUID parentId) {
        return responsavelRepo
            .findById(parentId)
            .orElseThrow(() -> new ResponsavelNotFoundException("Responsável não encontrado no banco de dados"));
    }
}
