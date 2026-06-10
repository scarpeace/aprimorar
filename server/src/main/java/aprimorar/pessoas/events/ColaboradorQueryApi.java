package aprimorar.pessoas.events;

import aprimorar.pessoas.dto.ColaboradorResponseDTO;

import java.util.UUID;

public interface ColaboradorQueryApi {
    ColaboradorResponseDTO findColaboradorById(UUID colaboradorId);
}
