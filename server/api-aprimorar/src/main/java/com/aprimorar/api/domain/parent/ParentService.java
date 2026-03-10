package com.aprimorar.api.domain.parent;

import com.aprimorar.api.domain.parent.dto.ParentSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentService {

    private final ParentRepository parentRepo;
    private final ParentMapper parentMapper;

    public ParentService(ParentRepository parentRepo, ParentMapper parentMapper) {
        this.parentRepo = parentRepo;
        this.parentMapper = parentMapper;
    }

    @Transactional(readOnly = true)
    public Page<ParentSummaryDTO> listParents(Pageable pageable, boolean includeArchived) {
        Page<Parent> parentPage = includeArchived
                ? parentRepo.findAll(pageable)
                : parentRepo.findAllByArchivedAtIsNull(pageable);
        return parentPage.map(parentMapper::toSummaryDto);
    }
}
