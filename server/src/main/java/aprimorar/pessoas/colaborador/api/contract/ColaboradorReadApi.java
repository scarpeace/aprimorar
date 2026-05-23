package aprimorar.pessoas.colaborador.api.contract;

import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ColaboradorReadApi {

    PageDTO<ColaboradorResponseDTO> getColaboradores(Pageable pageable, String busca, Boolean arquivado);

    ColaboradorResponseDTO buscarPorId(UUID colaboradorId);
}
