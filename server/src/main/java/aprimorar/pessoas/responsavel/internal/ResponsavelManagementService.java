package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.dto.ResponsavelOptionsDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface ResponsavelManagementService {

    ResponsavelResponseDTO createParent(ResponsavelRequestDTO dto);

    PageDTO<ResponsavelResponseDTO> getParents(Pageable pageable, String search, boolean archived);

    List<ResponsavelOptionsDTO> getParentOptions();

    ResponsavelResponseDTO findById(UUID parentId);

    ResponsavelResponseDTO updateParent(UUID parentId, ResponsavelRequestDTO dto);

    void archiveParent(UUID id);

    void unarchiveParent(UUID id);

    void deleteParent(UUID id);
}
