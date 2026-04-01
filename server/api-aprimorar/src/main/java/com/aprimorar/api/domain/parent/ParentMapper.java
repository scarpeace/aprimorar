package com.aprimorar.api.domain.parent;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.dto.ParentCreateDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentUpdateDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class ParentMapper {

    public Parent convertToEntityForCreate(ParentCreateDTO request) {
        Parent parent = new Parent();

        parent.setName(request.name());
        parent.setContact(MapperUtils.normalizeContact(request.contact()));
        parent.setEmail(MapperUtils.normalizeEmail(request.email()));
        parent.setCpf(MapperUtils.normalizeCpf(request.cpf()));

        return parent;
    }

public Parent convertToEntityForUpdate(ParentUpdateDTO request) {
        Parent parent = new Parent();

        parent.setName(request.name());
        parent.setContact(MapperUtils.normalizeContact(request.contact()));
        parent.setEmail(MapperUtils.normalizeEmail(request.email()));

        return parent;
    }

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
