package aprimorar.pessoas.shared.address.dto;

import aprimorar.pessoas.shared.address.BrazilianStatesEnum;

public record AddressRequestDTO(
    String street,
    String district,
    String city,
    BrazilianStatesEnum state,
    String zip,
    String complement
) {}
