package com.aprimorar.api.domain.parent;

import java.time.Clock;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.dto.UpdateParentDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class ParentMapper {

    private final Clock applicationClock;

    public ParentMapper(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }

    public ParentEntity convertToEntity(ParentRequestDTO dto) {

        ParentEntity entity = new ParentEntity();
        entity.setName(dto.name());
        entity.setEmail(MapperUtils.normalizeEmail(dto.email()));
        entity.setContact(MapperUtils.normalizeContact(dto.contact()));
        entity.setCpf(MapperUtils.normalizeCpf(dto.cpf()));
        entity.setCreatedAt(applicationClock.instant());
        return entity;
    }

    public ParentResponseDTO convertToDto(ParentEntity entity) {

        return new ParentResponseDTO(
                entity.getName(),
                entity.getEmail(),
                MapperUtils.formatContact(entity.getContact()),
                MapperUtils.formatCpf(entity.getCpf())
        );
    }

    public ParentEntity updateToEnity(UpdateParentDTO updateParentDTO) {

        ParentEntity entity = new ParentEntity();
        entity.setName(updateParentDTO.name());
        entity.setEmail(MapperUtils.normalizeEmail(updateParentDTO.email()));
        entity.setContact(MapperUtils.normalizeContact(updateParentDTO.contact()));
        entity.setCpf(MapperUtils.normalizeCpf(updateParentDTO.cpf()));
        entity.setUpdatedAt(applicationClock.instant());
        return entity;
    }
}
