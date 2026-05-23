package aprimorar.pessoas.aluno.api;

import java.util.UUID;

public interface AlunoResponsavelLinkApi {

    boolean hasStudentsLinkedToParent(UUID parentId);

    boolean hasActiveStudentsLinkedToParent(UUID parentId);
}
