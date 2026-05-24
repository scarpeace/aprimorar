package aprimorar.pessoas.colaborador.api.dto;

import org.springframework.data.domain.Page;

public record ColaboradoresResponseDTO(
    Long totalColaboradoresAtivos,
    Page<ColaboradorResponseDTO> colaboradores
) {
}
