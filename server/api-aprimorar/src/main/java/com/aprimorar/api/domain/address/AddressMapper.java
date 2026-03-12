package com.aprimorar.api.domain.address;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class AddressMapper {

    public AddressEntity convertToEntity(AddressRequestDTO dto) {
        return new AddressEntity(
                dto.street().trim(),
                dto.number().trim(),
                dto.district().trim(),
                dto.city().trim(),
                dto.state().trim(),
                MapperUtils.normalizeZip(dto.zip()),
                dto.complement().trim()
        );
    }

    public AddressResponseDTO convertToDto(AddressEntity entity) {

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
}
