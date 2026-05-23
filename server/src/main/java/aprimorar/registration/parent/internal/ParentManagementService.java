package aprimorar.registration.parent.internal;

import aprimorar.registration.parent.api.dto.ParentOptionsDTO;
import aprimorar.registration.parent.api.dto.ParentRequestDTO;
import aprimorar.registration.parent.api.dto.ParentResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface ParentManagementService {

    ParentResponseDTO createParent(ParentRequestDTO dto);

    PageDTO<ParentResponseDTO> getParents(Pageable pageable, String search, boolean archived);

    List<ParentOptionsDTO> getParentOptions();

    ParentResponseDTO findById(UUID parentId);

    ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO dto);

    void archiveParent(UUID id);

    void unarchiveParent(UUID id);

    void deleteParent(UUID id);
}
