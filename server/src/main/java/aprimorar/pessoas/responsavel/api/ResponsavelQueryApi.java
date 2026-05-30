package aprimorar.pessoas.responsavel.api;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

import aprimorar.pessoas.responsavel.internal.web.dto.ResponsaveisListDTO;
import aprimorar.pessoas.responsavel.internal.web.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;

public interface ResponsavelQueryApi {


    PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean archived);
    List<ResponsaveisListDTO> listResponsaveis();
    ResponsavelResponseDTO findResponsavelById(UUID id);

}
