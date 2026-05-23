package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.aluno.api.AlunoResponsavelLinkApi;
import aprimorar.pessoas.aluno.internal.repository.AlunoRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AlunoResponsavelLinkAdapter implements AlunoResponsavelLinkApi {

    private final AlunoRepository alunoRepository;

    public AlunoResponsavelLinkAdapter(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public boolean hasStudentsLinkedToParent(UUID parentId) {
        return alunoRepository.existsByParentId(parentId);
    }

    @Override
    public boolean hasActiveStudentsLinkedToParent(UUID parentId) {
        return alunoRepository.existsByParentIdAndActiveTrue(parentId);
    }
}
