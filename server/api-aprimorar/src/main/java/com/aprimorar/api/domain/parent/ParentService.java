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
        Page<Parent> parentPage = parentRepo.findAll(pageable);
        return parentPage.map(parentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public ParentResponseDTO findById(UUID parentId) {
        Parent parent = findParentByIdOrThrow(parentId);
        return parentMapper.convertToDto(parent);
    }

    /*
      ------------------------ COMMAND METHODS ------------------------
     */

    @Transactional
    public ParentResponseDTO createParent(ParentRequestDTO parentRequestDTO) {
        ParentCommand command = parentMapper.convertToCommand(parentRequestDTO);
        Parent parent = new Parent();
        parent.create(command);

        Parent savedParent = parentRepo.save(parent);
        return parentMapper.convertToDto(savedParent);
    }

    @Transactional
    public ParentResponseDTO updateParent(UUID parentId, ParentRequestDTO parentRequestDTO) {
        Parent foundParent = findParentByIdOrThrow(parentId);
        ParentCommand command = parentMapper.convertToCommand(parentRequestDTO);

        foundParent.updateDetails(command);

        return parentMapper.convertToDto(foundParent);
    }

    @Transactional
    public void deleteParent(UUID parentId) {
        Parent parent = findParentByIdOrThrow(parentId);
        parentRepo.delete(parent);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Parent findParentByIdOrThrow(UUID parentId) {
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new ParentNotFoundException("Responsável com o CPF informado não existe no banco de dados"));
    }
}
