package com.aprimorar.api.domain.parent;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.parent.command.ParentCommand;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class ParentMapper {

    public Parent convertToEntity(ParentRequestDTO dto) {
        ParentCommand command = convertToCommand(dto);
        Parent entity = new Parent();
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

    public Parent convertToEntity(ParentCommand command) {
        Parent entity = new Parent();
        entity.create(command);
        return entity;
    }

    public ParentResponseDTO convertToDto(Parent entity) {

        return new ParentResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                MapperUtils.formatContact(entity.getContact()),
                MapperUtils.formatCpf(entity.getCpf())
        );
    }
}
