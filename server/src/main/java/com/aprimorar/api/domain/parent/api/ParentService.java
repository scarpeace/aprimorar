package com.aprimorar.api.domain.parent.api;

import com.aprimorar.api.domain.parent.api.dto.ParentOptionsDTO;
import com.aprimorar.api.domain.parent.api.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.api.dto.ParentResponseDTO;
import com.aprimorar.api.shared.PageDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ParentService {

    ParentResponseDTO createParent(ParentRequestDTO request);

    PageDTO<ParentResponseDTO> getParents(Pageable pageable, String search, boolean archived);

    List<ParentOptionsDTO> getParentOptions();

    ParentResponseDTO findById(UUID parentId);

    Map<UUID, ParentResponseDTO> findByIds(Collection<UUID> parentIds);

    ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO request);

    void archiveParent(UUID id);

    void unarchiveParent(UUID id);

    void deleteParent(UUID id);
}
