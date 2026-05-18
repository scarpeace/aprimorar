package aprimorar.registration.shared.address;

import aprimorar.registration.shared.address.dto.AddressRequestDTO;
import aprimorar.registration.shared.address.dto.AddressResponseDTO;
import aprimorar.registration.shared.address.exception.InvalidAddressException;
import aprimorar.registration.shared.enums.BrazilianStates;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Address implements Serializable {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrazilianStates state;

    @Column(name = "zip", nullable = false)
    private String zip;

    private String complement;

    public Address() {}

    public Address(String street, String district, String city, BrazilianStates state, String zip, String complement) {
        setStreet(street);
        setDistrict(district);
        setCity(city);
        setState(state);
        setZip(zip);
        setComplement(complement);
    }

    public static Address fromRequest(AddressRequestDTO dto) {
        return new Address(
            dto.street(),
            dto.district(),
            dto.city(),
            dto.state(),
            dto.zip(),
            dto.complement()
        );
    }

    public AddressResponseDTO toResponseDto() {
        String normalizedZip = Objects.requireNonNull(zip, "Zip should not be null");
        String formattedZip = normalizedZip.length() == 8
            ? normalizedZip.substring(0, 5) + "-" + normalizedZip.substring(5)
            : normalizedZip;

        return new AddressResponseDTO(
            street,
            district,
            city,
            state,
            formattedZip,
            complement
        );
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street == null || street.isBlank()) {
            throw new InvalidAddressException("A rua é obrigatória no Endereço");
        }
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        if (district == null || district.isBlank()) {
            throw new InvalidAddressException("O bairro é obrigatório no Endereço");
        }
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isBlank()) {
            throw new InvalidAddressException("A cidade é obrigatória no Endereço");
        }
        this.city = city;
    }

    public BrazilianStates getState() {
        return state;
    }

    public void setState(BrazilianStates state) {
        if (state == null) {
            throw new InvalidAddressException("O estado é obrigatório no Endereço");
        }
        this.state = state;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        if (zip == null || zip.isBlank()) {
            throw new InvalidAddressException("O CEP é obrigatório no Endereço");
        }
        this.zip = zip.replaceAll("\\D", "");
    }
}
