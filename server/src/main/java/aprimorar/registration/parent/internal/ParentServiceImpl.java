package aprimorar.registration.parent.internal;

import aprimorar.registration.parent.api.ParentService;
import aprimorar.registration.parent.api.dto.ParentOptionsDTO;
import aprimorar.registration.parent.api.dto.ParentRequestDTO;
import aprimorar.registration.parent.api.dto.ParentResponseDTO;
import aprimorar.registration.parent.api.exception.ParentAlreadyExistsException;
import aprimorar.registration.parent.api.exception.ParentNotFoundException;
import aprimorar.registration.parent.internal.repository.ParentRepository;
import aprimorar.registration.parent.internal.repository.ParentSpecifications;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentServiceImpl implements ParentService {

    private static final Logger log = LoggerFactory.getLogger(ParentServiceImpl.class);

    private final ParentRepository parentRepo;

    public ParentServiceImpl(
        ParentRepository parentRepo
    ) {
        this.parentRepo = parentRepo;
    }

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO dto) {

        Parent parent = new Parent(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.cpf(), dto.email());

        ensureParentUniqueness(parent.getCpf(), parent.getEmail());
        Parent savedParent = parentRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return savedParent.toResponseDto();
    }

    @Transactional(readOnly = true)
    public PageDTO<ParentResponseDTO> getParents(Pageable pageable, String search, boolean archived) {
        Specification<Parent> spec = ParentSpecifications.isNotGhost();

        // if (Boolean.TRUE.equals(archived)) {
        //     spec = spec.and(ParentSpecifications.archived());
        // }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ParentSpecifications.searchContainsIgnoreCase(search.trim()));
        }
        Page<Parent> parentsPage = parentRepo.findAll(spec, pageable);
        Page<ParentResponseDTO> parentsDtoPage = parentsPage.map(Parent::toResponseDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", parentsPage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<ParentOptionsDTO> getParentOptions() {
        List<Parent> list = parentRepo.findByActiveTrueOrderByNameAsc();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(parent -> new ParentOptionsDTO(parent.getId(), parent.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return parent.toResponseDto();
    }

    // @Transactional(readOnly = true)
    // public Map<UUID, ParentResponseDTO> findByIds(Collection<UUID> parentIds) {
    //     if (parentIds == null || parentIds.isEmpty()) {
    //         return Map.of();
    //     }
    //     return parentRepo.findAllById(parentIds)
    //         .stream()
    //         .collect(Collectors.toMap(Parent::getId, Parent::toResponseDto));
    // }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO dto) {
        Parent parent = findParentOrThrow(parentId);

        ensureParentUniquenessForUpdate(dto.email(), parentId);
        parent.update(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.email());

        log.info("Responsável {} atualizado com sucesso.", parent.getName().toUpperCase());
        return parent.toResponseDto();
    }

    @Transactional
    public void archiveParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        // ensureParentHasNoActiveStudents(id, "arquivar");
        parent.archive();
        log.info("Responsável {} arquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        parent.unarchive();
        log.info("Responsável {} desarquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void deleteParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        // ensureParentHasNoActiveStudents(id, "excluir");
        parentRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Parent findParentOrThrow(UUID parentId) {
        return parentRepo
            .findById(parentId)
            .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
    }


    //TODO: Esse método tem que existir pra não arquivar um responsável com alunos ativos vinculados
    // private void ensureParentHasNoActiveStudents(UUID parentId, String action) {
    //     if (studentService.f) {
    //         throw new ParentHasLinkedStudentsException(
    //             "Não é possível %s um responsável com alunos ativos vinculados.".formatted(action)
    //         );
    //     }
    // }

    private void ensureParentUniqueness(String cpf, String email) {
        if (parentRepo.existsByCpf(cpf)) {
            throw new ParentAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if (parentRepo.existsByEmail(email)) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }

    private void ensureParentUniquenessForUpdate(String email, UUID parentId) {
        if (parentRepo.existsByEmailAndIdNot(email, parentId)) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }
}
