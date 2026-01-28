package com.aprimorar.api.controller.dto;

public record AddressRequestDto(String street,
                                String district,
                                String city,
                                String state,
                                String zipCode) {
}
