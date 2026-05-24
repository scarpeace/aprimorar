package aprimorar.pessoas.responsavel.api;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

import aprimorar.pessoas.responsavel.api.dto.ResponsavelOptionsDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.shared.PageDTO;

public interface ResponsavelService {

    ResponsavelResponseDTO createResponsavel(ResponsavelRequestDTO request);

    PageDTO<ResponsavelResponseDTO> getResponsaveis(Pageable pageable, String search, Boolean archived);

    List<ResponsavelOptionsDTO> listResponsaveis();

    ResponsavelResponseDTO findResponsavelById(UUID id);

    ResponsavelResponseDTO updateResponsavel(UUID id, ResponsavelRequestDTO request);

    void archiveResponsavel(UUID id);

    void unarchiveResponsavel(UUID id);

    void deleteResponsavel(UUID id);
}
