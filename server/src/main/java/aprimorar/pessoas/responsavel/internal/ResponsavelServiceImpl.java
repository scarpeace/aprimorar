package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.ResponsavelService;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelOptionsDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ResponsavelServiceImpl implements ResponsavelService {

    private final ResponsavelQueryService responsavelQueryService;
    private final ResponsavelMutationService responsavelMutationService;

    public ResponsavelServiceImpl(
        ResponsavelQueryService responsavelQueryService,
        ResponsavelMutationService responsavelMutationService
    ) {
        this.responsavelQueryService = responsavelQueryService;
        this.responsavelMutationService = responsavelMutationService;
    }

    @Override
    public ResponsavelResponseDTO createResponsavel(ResponsavelRequestDTO dto) {
        return responsavelMutationService.createResponsavel(dto);
    }

    @Override
    public PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean archived) {
        return responsavelQueryService.getResponsaveis(pageable, search, archived);
    }

    @Override
    public List<ResponsavelOptionsDTO> listResponsaveis() {
        return responsavelQueryService.listResponsaveis();
    }

    @Override
    public ResponsavelResponseDTO findResponsavelById(UUID parentId) {
        return responsavelQueryService.findResponsavelById(parentId);
    }

    @Override
    public ResponsavelResponseDTO updateResponsavel(UUID parentId, ResponsavelRequestDTO dto) {
        return responsavelMutationService.updateResponsavel(parentId, dto);
    }

    @Override
    public void archiveResponsavel(UUID id) {
        responsavelMutationService.archiveResponsavel(id);
    }

    @Override
    public void unarchiveResponsavel(UUID id) {
        responsavelMutationService.unarchiveResponsavel(id);
    }

    @Override
    public void deleteResponsavel(UUID id) {
        responsavelMutationService.deleteResponsavel(id);
    }
}
