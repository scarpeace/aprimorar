package com.aprimorar.api.domain.address.dto;

public record AddressResponseDTO(String street,
                                 String number,
                                 String complement,
                                 String district,
                                 String city,
                                 String state,
                                 String zip) {}
