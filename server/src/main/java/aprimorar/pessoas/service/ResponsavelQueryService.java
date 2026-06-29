package aprimorar.pessoas.service;


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

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.responsavel.ResponsavelFiltroRequest;
import aprimorar.pessoas.dto.responsavel.ResponsavelResponseDTO;
import aprimorar.pessoas.repository.ResponsavelRepository;
import aprimorar.pessoas.repository.specifications.ResponsavelSpecifications;

@Service
public class ResponsavelQueryService {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelQueryService.class);

    private final ResponsavelRepository responsavelRepo;

    public ResponsavelQueryService(ResponsavelRepository responsavelRepo) {
        this.responsavelRepo = responsavelRepo;
    }

    @Transactional(readOnly = true)
    public Page<ResponsavelResponseDTO> getResponsaveis(ResponsavelFiltroRequest filtro, Pageable pageable) {
        Specification<Responsavel> spec = ResponsavelSpecifications.comFiltros(filtro);
        Page<Responsavel> responsaveisPage = responsavelRepo.findAll(spec, pageable);
        Page<ResponsavelResponseDTO> responsaveisResponseDtoPage = responsaveisPage.map(ResponsavelResponseDTO::toDto);


        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", responsaveisPage.getTotalElements());
        return responsaveisResponseDtoPage;
    }

    @Transactional(readOnly = true)
    public List<ResponsavelResponseDTO> getResponsaveisList() {
        List<Responsavel> list = responsavelRepo.findAll();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(ResponsavelResponseDTO::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findResponsavelById(UUID responsavelId) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);
        log.info("Responsável {} consultado com sucesso.", responsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(responsavel);
    }

    private Responsavel findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo
            .findById(responsavelId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Responsável não encontrado no banco de dados"));
    }
}
