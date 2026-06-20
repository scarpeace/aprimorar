package aprimorar.pessoas.api;

import java.util.UUID;

public interface AlunoQueryApi {
    boolean existsById(UUID studentId);
    String getNomeById(UUID studentId);
}
