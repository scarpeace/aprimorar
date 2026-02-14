package com.aprimorar.api.controller.dto;

public record AddressResponseDto(String street,
                                 String district,
                                 String city,
                                 String state,
                                 String zipCode) {
}