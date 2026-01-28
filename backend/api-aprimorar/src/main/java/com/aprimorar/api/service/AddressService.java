package com.aprimorar.api.service;

import com.aprimorar.api.controller.dto.AddressRequestDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepo;

    public AddressService(AddressRepository addressRepo) {
        this.addressRepo = addressRepo;
    }

    public Address createAddress(AddressRequestDto addressRequestDto){
        Address newAddress = new Address(
                null,
                null,
                addressRequestDto.street(),
                addressRequestDto.district(),
                addressRequestDto.city(),
                addressRequestDto.state(),
                addressRequestDto.zipCode());

        return addressRepo.save(newAddress);
    }
}
