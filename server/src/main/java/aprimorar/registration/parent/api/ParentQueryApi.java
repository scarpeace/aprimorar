package aprimorar.registration.parent.api;

import aprimorar.registration.parent.api.dto.ParentResponseDTO;
import java.util.UUID;

public interface ParentQueryApi {

    ParentResponseDTO findById(UUID parentId);
}
