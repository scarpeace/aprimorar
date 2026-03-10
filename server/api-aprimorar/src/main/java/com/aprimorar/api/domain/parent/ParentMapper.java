package com.aprimorar.api.domain.parent;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.dto.CreateParentDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentSummaryDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class ParentMapper {

    private final MapperUtils mapperUtils;

    public ParentMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public Parent toEntity(CreateParentDTO dto) {
        if (dto == null) {
            return null;
        }

        Parent entity = new Parent();
        entity.setName(dto.name());
        entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        return entity;
    }

    public ParentResponseDTO toDto(Parent entity) {
        if (entity == null) {
            return null;
        }

        return new ParentResponseDTO(
                entity.getName(),
                entity.getEmail(),
                mapperUtils.formatContact(entity.getContact()),
                mapperUtils.formatCpf(entity.getCpf())
        );
    }

    public ParentSummaryDTO toSummaryDto(Parent entity) {
        if (entity == null) {
            return null;
        }

        return new ParentSummaryDTO(entity.getId(), entity.getName());
    }

    public void updateFromDto(CreateParentDTO dto, Parent entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.email() != null) {
            entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        }
        if (dto.contact() != null) {
            entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        }
        if (dto.cpf() != null) {
            entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        }
    }
}
