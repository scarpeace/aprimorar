package com.aprimorar.api.controller.dto;

public record AddressResponseDto(String street,
                                 String number,
                                 String complement,
                                 String district,
                                 String city,
                                 String state,
                                 String zipCode) {
}