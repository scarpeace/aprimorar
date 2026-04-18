package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;

@Component
public class StudentMapper {

    private final Clock applicationClock;
    private final AddressMapper addressMapper;

    public StudentMapper(Clock applicationClock, AddressMapper addressMapper) {
        this.applicationClock = applicationClock;
        this.addressMapper = addressMapper;
    }

    public StudentResponseDTO convertToDto(Student entity) {
        return new StudentResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getContact(),
            entity.getEmail(),
            entity.getCpf(),
            entity.getBirthdate(),
            entity.getSchool(),
            calculateAge(entity.getBirthdate()),
            addressMapper.convertToDto(entity.getAddress()),
            entity.getParent().getId(),
            entity.getArchivedAt(),
            entity.getUpdatedAt(),
            entity.getCreatedAt()
        );
    }

    public Integer calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now(applicationClock);
        return Period.between(birthdate, today).getYears();
    }
}
