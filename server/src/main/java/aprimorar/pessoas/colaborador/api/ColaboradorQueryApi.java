package aprimorar.pessoas.colaborador.api;

import aprimorar.pessoas.colaborador.internal.Colaborador;
import aprimorar.pessoas.colaborador.internal.repository.ColaboradorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class ColaboradorQueryApi {

    private final ColaboradorRepository colaboradorRepo;

    public ColaboradorQueryApi(ColaboradorRepository colaboradorRepo) {
        this.colaboradorRepo = colaboradorRepo;
    }

    public Page<Colaborador> getColaboradores(Pageable pageable, String busca, Boolean arquivado) {
        return colaboradorRepo.findAll(pageable);
    }

    public Colaborador findColaboradorById(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId).orElse(null);
    }
}
