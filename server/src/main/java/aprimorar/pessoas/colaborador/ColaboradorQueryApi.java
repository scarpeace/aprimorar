package aprimorar.pessoas.colaborador;

import aprimorar.pessoas.colaborador.web.dto.ColaboradoresListDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradoresResponseDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ColaboradorQueryApi {

    ColaboradoresResponseDTO getColaboradores(Pageable pageable, String busca, Boolean arquivado);

    List<ColaboradoresListDTO> listColaboradores();

    ColaboradorResponseDTO findColaboradorById(UUID colaboradorId);
}
