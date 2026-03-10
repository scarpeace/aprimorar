package com.aprimorar.api.domain.address;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.address.dto.UpdateAddressDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class AddressMapper {

    private final MapperUtils mapperUtils;

    public AddressMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public AddressEntity toEntity(AddressRequestDTO dto) {

        AddressEntity entity = new AddressEntity();
        entity.setStreet(dto.street());
        entity.setNumber(dto.number());
        entity.setComplement(dto.complement());
        entity.setDistrict(dto.district());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setZip(MapperUtils.sanitizeZip(dto.zip()));
        return entity;
    }

    public AddressResponseDTO toDto(AddressEntity entity) {

        return new AddressResponseDTO(
                entity.getStreet(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getState(),
                MapperUtils.formatZip(entity.getZip())
        );
    }

   public AddressEntity updateToEntity(UpdateAddressDTO dto) {

        AddressEntity entity = new AddressEntity();
        entity.setStreet(dto.street());
        entity.setNumber(dto.number());
        entity.setComplement(dto.complement());
        entity.setDistrict(dto.district());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setZip(MapperUtils.sanitizeZip(dto.zip()));
        return entity;
    }
}
