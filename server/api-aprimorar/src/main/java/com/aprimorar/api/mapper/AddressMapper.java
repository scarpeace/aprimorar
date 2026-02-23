package com.aprimorar.api.mapper;


import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(CreateAddressDTO dto);

    AddressResponseDTO toDto(Address entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateAddressDTO dto, @MappingTarget Address entity);

}
