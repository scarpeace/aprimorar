package com.aprimorar.api.dto.address;

public record AddressResponseDto(String street,
                                 String number,
                                 String complement,
                                 String district,
                                 String city,
                                 String state,
                                 String zipCode) {
}