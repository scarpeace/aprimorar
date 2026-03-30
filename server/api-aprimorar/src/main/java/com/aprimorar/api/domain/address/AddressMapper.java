package com.aprimorar.api.domain.address;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class AddressMapper {

    public Address convertToEntity(AddressRequestDTO dto) {
        Address address = new Address();
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setDistrict(dto.district());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZip(MapperUtils.normalizeZip(dto.zip()));

        return address;
    }

    public AddressResponseDTO convertToDto(Address entity) {

        return new AddressResponseDTO(
                entity.getStreet(),
                entity.getNumber(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getState(),
                MapperUtils.formatZip(entity.getZip()),
                entity.getComplement()
        );
    }
}
