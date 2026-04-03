package com.aprimorar.api.domain.address;

import com.aprimorar.api.domain.address.exception.InvalidAddressException;
import com.aprimorar.api.enums.BrazilianState;
import jakarta.persistence.*;

@Embeddable
public class Address {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrazilianState state;

    @Column(name = "zip", nullable = false)
    private String zip;

    private String complement;

    public Address() {}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street == null || street.isBlank()) {
            throw new InvalidAddressException("A rua é obrigatória no Endereço");
        }
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new InvalidAddressException("O numero é obrigatório no Endereço");
        }
        this.number = number;
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

    public BrazilianState getState() {
        return state;
    }

    public void setState(BrazilianState state) {
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
        this.zip = zip;
    }
}
