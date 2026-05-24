package aprimorar.pessoas.colaborador.api;

import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ColaboradorQueryApi {
    ColaboradoresResponseDTO getColaboradores(Pageable pageable, String busca, Boolean arquivado);
    ColaboradorResponseDTO findColaboradorById(UUID colaboradorId);
}
