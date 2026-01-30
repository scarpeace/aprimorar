package com.aprimorar.api.controller.dto;

import com.aprimorar.api.enums.Activity;

import java.util.Date;

public record StudentRequestDto(String name,
                               Date birthdate,
                               String cpf,
                               String school,
                                String phone,
                                Activity activity,
                                AddressRequestDto address,
                                ParentRequestDto parent
                               ) {
}
