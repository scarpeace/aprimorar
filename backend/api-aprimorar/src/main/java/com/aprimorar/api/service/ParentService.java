package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.ParentRequestDto;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.repository.ParentRepository;
import org.springframework.stereotype.Service;

@Service
public class ParentService {

    private final ParentRepository parentRepo;

    public ParentService(ParentRepository parentRepo) {
        this.parentRepo = parentRepo;
    }

    public Parent createParent(ParentRequestDto parentRequestDto){
        Parent newParent = new Parent(
                null,
                null,
                parentRequestDto.name(),
                parentRequestDto.email(),
                null,
                null);

        return parentRepo.save(newParent);
    }
}
