package com.aprimorar.api.domain.parent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.student.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;

/**
 * Centraliza as regras de negócio do responsável.
 *
 * <p>Aqui ficam a criação, atualização, consultas e ações de arquivar/desarquivar.
 * Também é esse service que garante que CPF e email não se repitam antes de salvar
 * ou atualizar um responsável.
 *
 * <p>Quando um responsável é alterado, o service reaproveita a entidade já persistida,
 * aplica as validações necessárias e devolve a resposta em formato DTO.
 *
 * @author scarpellini
 * @version 1.0
 * @since 2026-03-14
 */
@Service
public class ParentService {

    private static final Logger log = LoggerFactory.getLogger(ParentService.class);
    private static final UUID GHOST_PARENT_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

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
    public List<ParentResponseDTO> getParents() {
        List<Parent> list = parentRepo.findAll();
        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", list.size());
        return list.stream().map(parentMapper::convertToDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<ParentResponseDTO> getPaginatedParents(Pageable pageable, String search) {
        Page<Parent> page;
        if (search != null && !search.trim().isEmpty()) {
            Specification<Parent> spec = ParentSpecifications.searchContainsIgnoreCase(search.trim());
            page = parentRepo.findAll(spec, pageable);
        } else {
            page = parentRepo.findAll(pageable);
        }
        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(parentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    /* ----- Command Methods ----- */

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO request) {

        Parent parent = parentMapper.convertToEntity(request);

        verifyParentUniquenessForCreate(
                request.cpf(),
                request.email()
        );
        ParentRules.validate(parent);
        Parent savedParent = parentRepo.save(parent);

        log.info("Responsável {} cadastrado com sucesso.", savedParent.getName().toUpperCase());
        return parentMapper.convertToDto(savedParent);
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO request) {

        Parent newParent = parentMapper.convertToEntity(request);
        Parent updatedParent = findParentOrThrow(parentId);
        verifyParentUniquenessForUpdate(
                newParent.getCpf(),
                newParent.getEmail(),
                parentId
        );
        updatedParent.setName(newParent.getName());
        updatedParent.setEmail(newParent.getEmail());
        updatedParent.setContact(newParent.getContact());
        updatedParent.setCpf(newParent.getCpf());
        ParentRules.validate(updatedParent);

        log.info("Responsável {} atualizado com sucesso.", updatedParent.getName().toUpperCase());
        return parentMapper.convertToDto(updatedParent);
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
        if (GHOST_PARENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível deletar o registro de sistema 'RESPONSÁVEL FANTASMA'.");
        }
        Parent parent = findParentOrThrow(id);

        if (studentRepo.existsByParentId(id)) {
            throw new ParentHasLinkedStudentsException(
                    "Não é possível excluir um responsável com alunos vinculados. Primeiro, remova o vínculo ou exclua os alunos."
            );
        }

        parentRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */

    private Parent findParentOrThrow(UUID parentId) {
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
    }


    private void verifyParentUniquenessForUpdate(String cpf, String email, UUID id) {

        if(id != null) {
            if (parentRepo.existsByCpfAndIdNot(cpf, id)) {
                throw new ParentAlreadyExistsException(
                        "Responsável com o CPF informado já existe no banco de dados"
                );
            }
            if (parentRepo.existsByEmailAndIdNot(email, id)) {
                throw new ParentAlreadyExistsException(
                        "Responsável com o Email informado já existe no banco de dados"
                );
            }
        }
    }

    private void verifyParentUniquenessForCreate(String cpf, String email) {

        boolean existsByCpf = parentRepo.existsByCpf(cpf);
        boolean existsByEmail = parentRepo.existsByEmail(email);

        if(existsByCpf){
            throw new ParentAlreadyExistsException("Responsável com o CPF informado já existe no banco de dados");
        }

        if(existsByEmail){
            throw new ParentAlreadyExistsException("Responsável com o Email informado já existe no banco de dados");
        }
    }
}
