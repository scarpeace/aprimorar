package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.AddressRequestDto;
import com.aprimorar.api.controller.dto.AddressResponseDto;
import com.aprimorar.api.entity.Address;

public class AddressMapper {

    public static AddressResponseDto toDto(Address address){
        return new AddressResponseDto(
                address.getStreet(),
                address.getDistrict(),
                address.getCity(),
                address.getState(),
                address.getZipCode());
    }

}
