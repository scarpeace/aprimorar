package com.aprimorar.api.domain.registration.parent.internal;

import com.aprimorar.api.domain.registration.parent.api.ParentService;
import com.aprimorar.api.domain.registration.parent.api.dto.ParentOptionsDTO;
import com.aprimorar.api.domain.registration.parent.api.dto.ParentRequestDTO;
import com.aprimorar.api.domain.registration.parent.api.dto.ParentResponseDTO;
import com.aprimorar.api.domain.registration.parent.api.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.registration.parent.api.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.registration.parent.api.exception.ParentNotFoundException;
import com.aprimorar.api.domain.registration.parent.internal.repository.ParentRepository;
import com.aprimorar.api.domain.registration.parent.internal.repository.ParentSpecifications;
import com.aprimorar.api.domain.registration.student.api.StudentService;
import com.aprimorar.api.shared.MapperUtils;
import com.aprimorar.api.shared.PageDTO;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
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
    private final ObjectProvider<StudentService> studentService;

    public ParentServiceImpl(
        ParentRepository parentRepo,
        ObjectProvider<StudentService> studentService
    ) {
        this.parentRepo = parentRepo;
        this.studentService = studentService;
    }

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO request) {
        String normalizedEmail = MapperUtils.normalizeEmail(request.email());
        String normalizedContact = MapperUtils.normalizeContact(request.contact());
        String normalizedCpf = MapperUtils.normalizeCpf(request.cpf());

        Parent parent = new Parent(request.name(), normalizedEmail, normalizedContact, normalizedCpf);

        ensureParentUniqueness(parent.getCpf(), parent.getEmail());
        Parent savedParent = parentRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return savedParent.toResponseDto();
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
        Page<ParentResponseDTO> parentsDtoPage = parentsPage.map(Parent::toResponseDto);

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
        return parent.toResponseDto();
    }

    @Transactional(readOnly = true)
    public Map<UUID, ParentResponseDTO> findByIds(Collection<UUID> parentIds) {
        if (parentIds == null || parentIds.isEmpty()) {
            return Map.of();
        }
        return parentRepo.findAllById(parentIds)
            .stream()
            .collect(Collectors.toMap(Parent::getId, Parent::toResponseDto));
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO request) {
        Parent parent = findParentOrThrow(parentId);
        String normalizedEmail = MapperUtils.normalizeEmail(request.email());
        String normalizedContact = MapperUtils.normalizeContact(request.contact());
        ensureParentUniquenessForUpdate(normalizedEmail, parentId);

        parent.updateDetails(request.name(), normalizedEmail, normalizedContact);

        log.info("Responsável {} atualizado com sucesso.", parent.getName().toUpperCase());
        return parent.toResponseDto();
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
        if (studentService.getObject().hasActiveLinkedStudents(parentId)) {
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
}
