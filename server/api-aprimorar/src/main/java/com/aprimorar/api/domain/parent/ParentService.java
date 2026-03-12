package com.aprimorar.api.domain.parent;

import java.util.UUID;

import com.aprimorar.api.domain.parent.command.ParentCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;

@Service
public class ParentService {

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
        Page<ParentEntity> parentPage = parentRepo.findAll(pageable);
        return parentPage.map(parentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        ParentEntity parentEntity = findParentByIdOrThrow(parentId);
        return parentMapper.convertToDto(parentEntity);
    }

    /*
      ------------------------ COMMAND METHODS ------------------------
     */

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO parentRequestDTO) {
        ParentCommand command = parentMapper.convertToCommand(parentRequestDTO);
        ParentEntity parentEntity = new ParentEntity();
        parentEntity.create(command);

        ParentEntity savedParentEntity = parentRepo.save(parentEntity);
        return parentMapper.convertToDto(savedParentEntity);
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO parentRequestDTO) {
        ParentEntity foundParentEntity = findParentByIdOrThrow(parentId);
        ParentCommand command = parentMapper.convertToCommand(parentRequestDTO);

        foundParentEntity.updateDetails(command);

        return parentMapper.convertToDto(foundParentEntity);
    }

    @Transactional
    public void deleteParent(UUID parentId) {
        ParentEntity parentEntity = findParentByIdOrThrow(parentId);
        parentRepo.delete(parentEntity);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private ParentEntity findParentByIdOrThrow(UUID parentId) {
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new ParentNotFoundException(parentId));
    }
}
