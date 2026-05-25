package aprimorar.pessoas.colaborador.api;

import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresListDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresResponseDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ColaboradorQueryApi {

    ColaboradoresResponseDTO getColaboradores(Pageable pageable, String busca, Boolean arquivado);

    List<ColaboradoresListDTO> listColaboradores();

    ColaboradorResponseDTO findColaboradorById(UUID colaboradorId);
}
