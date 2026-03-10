package com.aprimorar.api.domain.address;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.address.dto.CreateAddressDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class AddressMapper {

    private final MapperUtils mapperUtils;

    public AddressMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public AddressEntity toEntity(CreateAddressDTO dto) {
        if (dto == null) {
            return null;
        }

        AddressEntity entity = new AddressEntity();
        entity.setStreet(dto.street());
        entity.setNumber(dto.number());
        entity.setComplement(dto.complement());
        entity.setDistrict(dto.district());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setZip(mapperUtils.sanitizeZip(dto.zip()));
        return entity;
    }

    public AddressResponseDTO toDto(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        return new AddressResponseDTO(
                entity.getStreet(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getDistrict(),
                entity.getCity(),
                entity.getState(),
                mapperUtils.formatZip(entity.getZip())
        );
    }

    public void updateFromDto(CreateAddressDTO dto, AddressEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.street() != null) {
            entity.setStreet(dto.street());
        }
        if (dto.number() != null) {
            entity.setNumber(dto.number());
        }
        if (dto.complement() != null) {
            entity.setComplement(dto.complement());
        }
        if (dto.district() != null) {
            entity.setDistrict(dto.district());
        }
        if (dto.city() != null) {
            entity.setCity(dto.city());
        }
        if (dto.state() != null) {
            entity.setState(dto.state());
        }
        if (dto.zip() != null) {
            entity.setZip(mapperUtils.sanitizeZip(dto.zip()));
        }
    }
}
