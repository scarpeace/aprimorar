package com.aprimorar.api.domain.address;

import com.aprimorar.api.enums.BrazilianState;
import jakarta.persistence.*;

@Embeddable
public class Address {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number",nullable = false)
    private String number;

    @Column(name = "district",nullable = false)
    private String district;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "state",nullable = false)
    @Enumerated(EnumType.STRING)
    private BrazilianState state;

    @Column(name = "zip",nullable = false)
    private String zip;

    private String complement;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BrazilianState getState() {
        return state;
    }

    public void setState(BrazilianState state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

}
