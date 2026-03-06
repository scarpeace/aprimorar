package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.util.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final MapperUtils mapperUtils;

    public AddressMapper(MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public Address toEntity(CreateAddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address entity = new Address();
        entity.setStreet(dto.street());
        entity.setNumber(dto.number());
        entity.setComplement(dto.complement());
        entity.setDistrict(dto.district());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setZip(mapperUtils.sanitizeZip(dto.zip()));
        return entity;
    }

    public AddressResponseDTO toDto(Address entity) {
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

    public void updateFromDto(CreateAddressDTO dto, Address entity) {
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
