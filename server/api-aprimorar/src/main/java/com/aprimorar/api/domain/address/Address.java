package com.aprimorar.api.domain.address;

import com.aprimorar.api.enums.BrazilianState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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


}
