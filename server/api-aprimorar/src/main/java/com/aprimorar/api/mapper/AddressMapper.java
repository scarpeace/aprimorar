package com.aprimorar.api.mapper;


import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.entity.Address;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {

    Address toEntity(CreateAddressDTO dto);

    AddressResponseDTO toDto(Address entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateAddressDTO dto, @MappingTarget Address entity);
}
