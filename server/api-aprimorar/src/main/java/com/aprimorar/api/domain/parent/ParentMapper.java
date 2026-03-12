package com.aprimorar.api.domain.parent;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.command.ParentCommand;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class ParentMapper {

    public ParentEntity convertToEntity(ParentRequestDTO dto) {
        ParentCommand command = convertToCommand(dto);
        ParentEntity entity = new ParentEntity();
        entity.create(command);
        return entity;
    }

    public ParentCommand convertToCommand(ParentRequestDTO dto) {
        return new ParentCommand(
                dto.name(),
                MapperUtils.normalizeEmail(dto.email()),
                MapperUtils.normalizeContact(dto.contact()),
                MapperUtils.normalizeCpf(dto.cpf())
        );
    }

    public ParentResponseDTO convertToDto(ParentEntity entity) {

        return new ParentResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                MapperUtils.formatContact(entity.getContact()),
                MapperUtils.formatCpf(entity.getCpf())
        );
    }
}
