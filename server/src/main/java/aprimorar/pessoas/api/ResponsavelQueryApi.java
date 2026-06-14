package aprimorar.pessoas.api;

import aprimorar.pessoas.dto.ResponsavelFiltroRequest;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResponsavelQueryApi {
    Page<ResponsavelResponseDTO> getResponsaveis(ResponsavelFiltroRequest filtro, Pageable pageable);
    ResponsavelResponseDTO findResponsavelById(UUID responsavelId);
    List<ResponsavelResponseDTO> getResponsaveisList();
}
