package aprimorar.pessoas.events;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

import aprimorar.pessoas.dto.ResponsaveisListDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;

public interface ResponsavelQueryApi {


    PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean archived);
    List<ResponsaveisListDTO> listResponsaveis();
    ResponsavelResponseDTO findResponsavelById(UUID id);

}
