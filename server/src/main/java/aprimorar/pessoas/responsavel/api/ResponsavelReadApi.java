package aprimorar.pessoas.responsavel.api;

import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import java.util.UUID;

public interface ResponsavelReadApi {

    ResponsavelResponseDTO findById(UUID parentId);
}
