package com.aprimorar.api.dto.address;

public record AddressResponseDTO(String street,
                                 String number,
                                 String complement,
                                 String district,
                                 String city,
                                 String state,
                                 String zipCode) {}