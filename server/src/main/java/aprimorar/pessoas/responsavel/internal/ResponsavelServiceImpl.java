package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.ResponsavelReadApi;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelOptionsDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelAlreadyExistsException;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelHasLinkedStudentsException;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelNotFoundException;
import aprimorar.pessoas.aluno.api.AlunoResponsavelLinkApi;
import aprimorar.pessoas.responsavel.internal.repository.ResponsavelRepository;
import aprimorar.pessoas.responsavel.internal.repository.ResponsavelSpecifications;
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
public class ResponsavelServiceImpl implements ResponsavelManagementService, ResponsavelReadApi {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelServiceImpl.class);

    private final ResponsavelRepository responsavelRepo;
    private final AlunoResponsavelLinkApi alunoResponsavelLinkApi;
    private final ResponsavelMapper responsavelMapper;

    public ResponsavelServiceImpl(
        ResponsavelRepository responsavelRepo,
        AlunoResponsavelLinkApi alunoResponsavelLinkApi,
        ResponsavelMapper responsavelMapper
    ) {
        this.responsavelRepo = responsavelRepo;
        this.alunoResponsavelLinkApi = alunoResponsavelLinkApi;
        this.responsavelMapper = responsavelMapper;
    }

    @Transactional
    public ResponsavelResponseDTO createParent(ResponsavelRequestDTO dto) {

        Responsavel parent = new Responsavel(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.cpf(), dto.email());

        ensureParentUniqueness(parent.getCpf(), parent.getEmail());
        Responsavel savedParent = responsavelRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return responsavelMapper.toResponseDto(savedParent);
    }

    @Transactional(readOnly = true)
    public PageDTO<ResponsavelResponseDTO> getParents(Pageable pageable, String search, boolean archived) {
        Specification<Responsavel> spec = ResponsavelSpecifications.isNotGhost();

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ResponsavelSpecifications.searchContainsIgnoreCase(search.trim()));
        }
        Page<Responsavel> parentsPage = responsavelRepo.findAll(spec, pageable);
        Page<ResponsavelResponseDTO> parentsDtoPage = parentsPage.map(responsavelMapper::toResponseDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", parentsPage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<ResponsavelOptionsDTO> getParentOptions() {
        List<Responsavel> list = responsavelRepo.findByActiveTrueOrderByNameAsc();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(parent -> new ResponsavelOptionsDTO(parent.getId(), parent.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findById(UUID parentId) {
        Responsavel parent = findResponsavelOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return responsavelMapper.toResponseDto(parent);
    }

    @Transactional
    public ResponsavelResponseDTO updateParent(UUID parentId, ResponsavelRequestDTO dto) {
        Responsavel parent = findResponsavelOrThrow(parentId);

        ensureParentUniquenessForUpdate(dto.email(), parentId);
        parent.updateDetails(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.email());

        log.info("Responsável {} atualizado com sucesso.", parent.getName().toUpperCase());
        return responsavelMapper.toResponseDto(parent);
    }

    @Transactional
    public void archiveParent(UUID id) {
        if (alunoResponsavelLinkApi.hasActiveStudentsLinkedToParent(id)) {
            throw new ResponsavelHasLinkedStudentsException(
                "O responsável possui alunos ativos vinculados. Arquive os alunos antes de arquivar o responsável."
            );
        }

        Responsavel parent = findResponsavelOrThrow(id);
        parent.archive();
        log.info("Responsável {} arquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveParent(UUID id) {
        Responsavel parent = findResponsavelOrThrow(id);
        parent.unarchive();
        log.info("Responsável {} desarquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void deleteParent(UUID id) {
        if (alunoResponsavelLinkApi.hasStudentsLinkedToParent(id)) {
            throw new ResponsavelHasLinkedStudentsException(
                "O responsável possui alunos vinculados. Reassocie ou remova os alunos antes de excluí-lo."
            );
        }

        Responsavel parent = findResponsavelOrThrow(id);
        responsavelRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Responsavel findResponsavelOrThrow(UUID parentId) {
        return responsavelRepo
            .findById(parentId)
            .orElseThrow(() -> new ResponsavelNotFoundException("Responsável não encontrado no banco de dados"));
    }

    private void ensureParentUniqueness(String cpf, String email) {
        if (responsavelRepo.existsByCpf(cpf)) {
            throw new ResponsavelAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if (responsavelRepo.existsByEmail(email)) {
            throw new ResponsavelAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }

    private void ensureParentUniquenessForUpdate(String email, UUID parentId) {
        if (responsavelRepo.existsByEmailAndIdNot(email, parentId)) {
            throw new ResponsavelAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }
}
