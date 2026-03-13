package com.aprimorar.api.domain.parent;

import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;

//TODO O MODELO É ESSE AQUI

@Service
public class ParentService {

    private static final Logger log = LoggerFactory.getLogger(ParentService.class);

    private final ParentRepository parentRepo;
    private final ParentMapper parentMapper;

    public ParentService(ParentRepository parentRepo, ParentMapper parentMapper) {
        this.parentRepo = parentRepo;
        this.parentMapper = parentMapper;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<ParentResponseDTO> getParents(Pageable pageable) {
        Page<Parent> page = parentRepo.findAll(pageable);
        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(parentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentOrThrow(parentId);
        log.info("Responsável {} consultado com sucesso.", parent.getName().toUpperCase());
        return parentMapper.convertToDto(parent);
    }

    /*
      ------------------------ COMMAND METHODS ------------------------
     */

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
        Parent oldParent = findParentOrThrow(parentId);
        verifyParentUniquenessForUpdate(
                newParent.getCpf(),
                newParent.getEmail(),
                parentId
        );
        oldParent.setName(newParent.getName());
        oldParent.setEmail(newParent.getEmail());
        oldParent.setContact(newParent.getContact());
        oldParent.setCpf(newParent.getCpf());
        ParentRules.validate(oldParent);

        log.info("Responsável {} atualizado com sucesso.", oldParent.getName().toUpperCase());
        return parentMapper.convertToDto(oldParent);
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
        parentRepo.delete(parent);
        log.info("Responsável {} deletado com sucesso.", parent.getName().toUpperCase());
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

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
