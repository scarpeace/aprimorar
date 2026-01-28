package com.aprimorar.api.controller.dto;

import java.util.UUID;

public record AddressResponseDto(UUID id,
                                 String street,
                                 String district,
                                 String city,
                                 String state,
                                 String zipCode) {
}