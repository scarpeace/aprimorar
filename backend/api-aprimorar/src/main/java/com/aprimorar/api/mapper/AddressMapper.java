package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.AddressRequestDto;
import com.aprimorar.api.controller.dto.AddressResponseDto;
import com.aprimorar.api.entity.Address;

public class AddressMapper {

    public static AddressResponseDto toDto(Address address){
        return new AddressResponseDto(
                address.getId(),
                address.getStreet(),
                address.getDistrict(),
                address.getCity(),
                address.getState(),
                address.getZipCode());
    }

    public static Address toEntity(AddressRequestDto addressRequestDto){
        return new Address(
                null,
                null,
                addressRequestDto.street(),
                addressRequestDto.district(),
                addressRequestDto.city(),
                addressRequestDto.state(),
                addressRequestDto.zipCode());
    }
}
