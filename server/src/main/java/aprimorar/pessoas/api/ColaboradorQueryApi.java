package aprimorar.pessoas.api;

import java.util.UUID;

public interface ColaboradorQueryApi {
    ColaboradorResponseDTO findColaboradorById(UUID colaboradorId);
}
