package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.StudentResponsibleSummaryDTO;

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
            convertResponsibleSummary(entity),
            entity.getArchivedAt(),
            entity.getUpdatedAt(),
            entity.getCreatedAt()
        );
    }

    private StudentResponsibleSummaryDTO convertResponsibleSummary(Student entity) {
        return new StudentResponsibleSummaryDTO(
            entity.getParent().getId(),
            entity.getParent().getName(),
            entity.getParent().getContact(),
            entity.getParent().getCpf()
        );
    }

    public Integer calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now(applicationClock);
        return Period.between(birthdate, today).getYears();
    }
}
