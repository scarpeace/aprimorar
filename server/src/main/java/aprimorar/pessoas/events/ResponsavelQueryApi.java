package aprimorar.pessoas.events;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import aprimorar.pessoas.dto.ResponsaveisListDTO;
import aprimorar.pessoas.dto.ResponsavelFiltroRequest;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;

public interface ResponsavelQueryApi {


    Page<ResponsavelResponseDTO> getResponsaveis(ResponsavelFiltroRequest filtro, Pageable pageable);
    List<ResponsaveisListDTO> listResponsaveis();
    ResponsavelResponseDTO findResponsavelById(UUID id);

}
