package com.aprimorar.api.controller.dto;

import java.util.Date;

public record StudentRequestDto(String name,
                               Date birthdate,
                               String cpf,
                               String school,
                                AddressRequestDto addressRequestDto,
                                ParentRequestDto parentRequestDto
                               ) {
}
