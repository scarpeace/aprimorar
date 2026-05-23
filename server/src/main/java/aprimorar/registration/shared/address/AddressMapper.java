package aprimorar.registration.shared.address;

import org.springframework.stereotype.Component;
import aprimorar.registration.shared.address.dto.AddressRequestDTO;
import aprimorar.registration.shared.address.dto.AddressResponseDTO;

@Component
public class AddressMapper {

    public static Address toEntity(AddressRequestDTO dto) {
        return new Address(
            dto.street(),
            dto.district(),
            dto.city(),
            dto.state(),
            dto.zip(),
            dto.complement()
        );
    }

    public static AddressResponseDTO toDto(Address address) {
        return new AddressResponseDTO(
            address.getStreet(),
            address.getDistrict(),
            address.getCity(),
            address.getState(),
            address.getZip(),
            address.getComplement()
        );
    }
}
