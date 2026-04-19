package com.aprimorar.api.domain.parent;

import com.aprimorar.api.domain.parent.dto.ParentOptionsDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.parent.repository.ParentSpecifications;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.shared.MapperUtils;
import com.aprimorar.api.shared.PageDTO;
import java.time.Instant;
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
public class ParentService {

    private static final Logger log = LoggerFactory.getLogger(ParentService.class);

    private final ParentRepository parentRepo;
    private final ParentMapper parentMapper;
    private final StudentRepository studentRepo;

    public ParentService(ParentRepository parentRepo, ParentMapper parentMapper, StudentRepository studentRepo) {
        this.parentRepo = parentRepo;
        this.parentMapper = parentMapper;
        this.studentRepo = studentRepo;
    }

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO request) {
        String normalizedEmail = normalizeEmail(request.email());
        String normalizedContact = normalizeContact(request.contact());
        String normalizedCpf = normalizeCpf(request.cpf());

        Parent parent = new Parent(request.name(), normalizedEmail, normalizedContact, normalizedCpf);

        ensureParentUniqueness(parent.getCpf(), parent.getEmail());
        Parent savedParent = parentRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return parentMapper.convertToDto(savedParent);
    }

    @Transactional(readOnly = true)
    public PageDTO<ParentResponseDTO> getParents(Pageable pageable, String search, boolean archived) {
        Specification<Parent> spec = ParentSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(archived)) {
            spec = spec.and(ParentSpecifications.archived());
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ParentSpecifications.searchContainsIgnoreCase(search.trim()));
        }
        Page<Parent> parentsPage = parentRepo.findAll(spec, pageable);
        Page<ParentResponseDTO> parentsDtoPage = parentsPage.map(parentMapper::convertToDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", parentsPage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public List<ParentOptionsDTO> getParentOptions() {
        List<Parent> list = parentRepo.findByArchivedAtIsNullOrderByNameAsc();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(p -> new ParentOptionsDTO(p.getId(), p.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO request) {
        Parent parent = findParentOrThrow(parentId);
        String normalizedEmail = normalizeEmail(request.email());
        String normalizedContact = normalizeContact(request.contact());
        ensureParentUniquenessForUpdate(normalizedEmail, parentId);

        parent.updateDetails(request.name(), normalizedEmail, normalizedContact);

        log.info("Responsável {} atualizado com sucesso.", parent.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    @Transactional
    public void archiveParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        ensureParentHasNoActiveStudents(id, "arquivar");
        parent.setArchivedAt(Instant.now());
        log.info("Responsável {} arquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        parent.setArchivedAt(null);
        log.info("Responsável {} desarquivado com sucesso.", parent.getName().toUpperCase());
    }

    @Transactional
    public void deleteParent(UUID id) {
        Parent parent = findParentOrThrow(id);
        ensureParentHasNoActiveStudents(id, "excluir");
        parentRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Parent findParentOrThrow(UUID parentId) {
        return parentRepo
            .findById(parentId)
            .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
    }

    private void ensureParentHasNoActiveStudents(UUID parentId, String action) {
        if (studentRepo.existsByParentIdAndArchivedAtIsNull(parentId)) {
            throw new ParentHasLinkedStudentsException(
                "Não é possível %s um responsável com alunos ativos vinculados.".formatted(action)
            );
        }
    }

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

    private String normalizeEmail(String email) {
        return MapperUtils.normalizeEmail(email);
    }

    private String normalizeContact(String contact) {
        return MapperUtils.normalizeContact(contact);
    }

    private String normalizeCpf(String cpf) {
        return MapperUtils.normalizeCpf(cpf);
    }
}
