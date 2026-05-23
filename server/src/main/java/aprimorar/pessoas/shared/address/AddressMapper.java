package aprimorar.pessoas.shared.address;

import org.springframework.stereotype.Component;
import aprimorar.pessoas.shared.address.dto.AddressRequestDTO;
import aprimorar.pessoas.shared.address.dto.AddressResponseDTO;
import java.util.Objects;

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
        String normalizedZip = Objects.requireNonNull(address.getZip(), "Zip should not be null");
        String formattedZip = normalizedZip.length() == 8
            ? normalizedZip.substring(0, 5) + "-" + normalizedZip.substring(5)
            : normalizedZip;

        return new AddressResponseDTO(
            address.getStreet(),
            address.getDistrict(),
            address.getCity(),
            address.getState(),
            formattedZip,
            address.getComplement()
        );
    }
}
