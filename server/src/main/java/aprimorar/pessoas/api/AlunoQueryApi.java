package aprimorar.pessoas.api;

import aprimorar.pessoas.dto.AlunoFiltroRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlunoQueryApi {
    boolean existsById(UUID studentId);
}
