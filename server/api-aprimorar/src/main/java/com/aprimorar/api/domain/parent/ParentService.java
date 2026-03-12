package com.aprimorar.api.domain.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
}
