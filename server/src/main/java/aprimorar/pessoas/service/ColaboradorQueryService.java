package aprimorar.pessoas.service;

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.dto.colaborador.ColaboradorFiltroRequest;
import aprimorar.pessoas.dto.colaborador.ColaboradorResponseDTO;
import aprimorar.pessoas.dto.colaborador.ColaboradoresOptionsDTO;
import aprimorar.pessoas.repository.ColaboradorRepository;
import aprimorar.pessoas.repository.specifications.ColaboradorSpecifications;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorQueryService {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorQueryService.class);

    private final ColaboradorRepository colaboradorRepo;
    private final UUID ghostColaboradorId;

    public ColaboradorQueryService(
        ColaboradorRepository colaboradorRepo,
        @Value("${aprimorar.ghost-colaborador-id}") String ghostColaboradorId
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.ghostColaboradorId = UUID.fromString(ghostColaboradorId);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorResponseDTO> getColaboradores(ColaboradorFiltroRequest filtro, Pageable pageable) {
        Specification<Colaborador> spec = ColaboradorSpecifications.comFiltros(filtro, ghostColaboradorId);
        Page<Colaborador> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);
        Page<ColaboradorResponseDTO> colaboradoresDtoPage = colaboradoresPage.map(ColaboradorResponseDTO::toDto);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresDtoPage;
    }

    @Transactional(readOnly = true)
    public ColaboradorResponseDTO findById(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresOptionsDTO> getColaboradoresOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        return colaboradorRepo
            .findAll(ColaboradorSpecifications.isNotArchived().and(ColaboradorSpecifications.isNotGhost(ghostColaboradorId)), sort)
            .stream()
            .map(e -> new ColaboradoresOptionsDTO(e.getId(), e.getNome()))
            .toList();
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado no banco de dados"));
    }


}
