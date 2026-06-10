package aprimorar.pessoas.shared.address.dto;

import aprimorar.pessoas.shared.address.BrazilianStatesEnum;

public record AddressResponseDTO(
    String street,
    String district,
    String city,
    BrazilianStatesEnum state,
    String zip,
    String complement
) {}
