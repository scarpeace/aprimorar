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

    public ParentEntity toEntity(CreateParentDTO dto) {
        if (dto == null) {
            return null;
        }

        ParentEntity entity = new ParentEntity();
        entity.setName(dto.name());
        entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        return entity;
    }

    public ParentResponseDTO toDto(ParentEntity entity) {
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

    public ParentSummaryDTO toSummaryDto(ParentEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ParentSummaryDTO(entity.getId(), entity.getName());
    }

    public void updateFromDto(CreateParentDTO dto, ParentEntity entity) {
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
