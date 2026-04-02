package com.aprimorar.api.domain.parent;

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

import com.aprimorar.api.domain.parent.dto.ParentCreateDTO;
import com.aprimorar.api.domain.parent.dto.ParentOptionsDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentUpdateDTO;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.parent.repository.ParentRepository;
import com.aprimorar.api.domain.parent.repository.ParentSpecifications;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.shared.PageDTO;

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

    /* ----- Query Methods ----- */
    @Transactional(readOnly = true)
    public List<ParentOptionsDTO> getParentOptions() {
        List<Parent> list = parentRepo.findByArchivedAtIsNull();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", list.size());
        return list
            .stream()
            .map(p -> new ParentOptionsDTO(p.getId(), p.getName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public PageDTO<ParentResponseDTO> getParents(Pageable pageable, String search) {
        Page<Parent> parentsPage;
        if (search != null && !search.trim().isEmpty()) {
            Specification<Parent> spec = ParentSpecifications.searchContainsIgnoreCase(search.trim());
            parentsPage = parentRepo.findAll(spec, pageable);
        } else {
            parentsPage = parentRepo.findAll(pageable);
        }
        Page<ParentResponseDTO> parentsDtoPage = parentsPage.map(parentMapper::convertToDto);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", parentsPage.getTotalElements());
        return new PageDTO<>(parentsDtoPage);
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    /* ----- Command Methods ----- */
    @Transactional
    public ParentResponseDTO createParent(ParentCreateDTO request) {
        Parent parent = parentMapper.convertToEntityForCreate(request);

        ensureParentUniqueness(parent);
        Parent savedParent = parentRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return parentMapper.convertToDto(savedParent);
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentUpdateDTO request) {
        Parent parent = findParentOrThrow(parentId);
        Parent updatedParentData = parentMapper.convertToEntityForUpdate(request);
        ensureParentUniquenessForUpdate(updatedParentData, parentId);

        parent.setName(updatedParentData.getName());
        parent.setEmail(updatedParentData.getEmail());
        parent.setContact(updatedParentData.getContact());

        log.info("Responsável {} atualizado com sucesso.", updatedParentData.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    @Transactional
    public void archiveParent(UUID id) {
        Parent parent = findParentOrThrow(id);
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
        ensureParentHasNoStudents(id);
        parentRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */
    private Parent findParentOrThrow(UUID parentId) {
        return parentRepo
            .findById(parentId)
            .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
    }

    private void ensureParentHasNoStudents(UUID parentId) {
        if (studentRepo.existsByParentId(parentId)) {
            throw new ParentHasLinkedStudentsException(
                "Não é possível excluir um responsável com alunos vinculados. Primeiro, exclua os alunos vinculados e tente novamente."
            );
        }
    }

    private void ensureParentUniqueness(Parent parent) {
        if (parentRepo.existsByCpf(parent.getCpf())) {
            throw new ParentAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if (parentRepo.existsByEmail(parent.getEmail())) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }

    private void ensureParentUniquenessForUpdate(Parent parent, UUID parentId) {
        if (parentRepo.existsByEmailAndIdNot(parent.getEmail(), parentId)) {
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }
}
