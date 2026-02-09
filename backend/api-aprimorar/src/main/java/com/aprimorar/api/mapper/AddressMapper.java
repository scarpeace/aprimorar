package com.aprimorar.api.mapper;


import com.aprimorar.api.controller.dto.AddressRequestDto;
import com.aprimorar.api.controller.dto.AddressResponseDto;
import com.aprimorar.api.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequestDto dto);

    AddressResponseDto toDto(Address entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AddressRequestDto dto, @MappingTarget Address entity);

}
