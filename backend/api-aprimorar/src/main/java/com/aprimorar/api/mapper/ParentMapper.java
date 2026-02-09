package com.aprimorar.api.mapper;


import com.aprimorar.api.controller.dto.ParentRequestDto;
import com.aprimorar.api.controller.dto.ParentResponseDto;
import com.aprimorar.api.entity.Parent;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ParentMapper {

    Parent toEntity(ParentRequestDto dto);

    ParentResponseDto toDto(Parent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ParentRequestDto dto, @MappingTarget Parent entity);

}
