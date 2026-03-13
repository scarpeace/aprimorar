package com.aprimorar.api.domain.address;

import com.aprimorar.api.domain.address.exception.InvalidAddressException;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;

public class AddressRules {

    public static void validate(Address address) {
        validateRequiredFields(address);
    }

    private static void validateRequiredFields(Address address) {
        if (address.getStreet() == null || address.getStreet().isBlank()) {
            throw new InvalidAddressException("A rua é obrigatória no Endereço");
        }
        if (address.getNumber() == null || address.getNumber().isBlank()){
            throw new InvalidAddressException("O numero é obrigatório no Endereço");
        }
        if (address.getDistrict() == null || address.getDistrict().isBlank()) {
            throw new InvalidAddressException("O bairro é obrigatório no Endereço");
        }
        if (address.getCity() == null || address.getCity().isBlank()) {
            throw new InvalidAddressException("A cidade é obrigatória no Endereço");
        }
        if (address.getState() == null) {
            throw new InvalidAddressException("O estado é obrigatório no Endereço");
        }
        if (address.getZip() == null || address.getZip().isBlank()) {
            throw new InvalidAddressException("O CEP é obrigatório no Endereço");
        }
    }
}
