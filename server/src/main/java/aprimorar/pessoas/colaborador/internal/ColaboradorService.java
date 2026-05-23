package aprimorar.pessoas.colaborador.internal;

import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorCountSummaryDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorOptionsDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ColaboradorService {

    ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto);

    PageDTO<ColaboradorResponseDTO> getColaboradores(Pageable pageable, String busca, Boolean arquivado);

    ColaboradorResponseDTO buscarPorId(UUID colaboradorId);

    ColaboradorCountSummaryDTO getSummary();

    List<ColaboradorOptionsDTO> listarOpcoes();

    ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto);

    void deleteColaborador(UUID colaboradorId);

    void archiveColaborador(UUID colaboradorId);

    void unarchiveColaborador(UUID colaboradorId);
}
