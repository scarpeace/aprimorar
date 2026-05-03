package com.aprimorar.api.domain.parent;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

@Component
public class ParentMapper {

    public ParentResponseDTO convertToDto(Parent parent) {

        return new ParentResponseDTO(
                parent.getId(),
                parent.getName(),
                parent.getEmail(),
                parent.getContact(),
                parent.getCpf(),
                parent.getArchivedAt(),
                parent.getCreatedAt(),
                parent.getUpdatedAt()
        );
    }
}
