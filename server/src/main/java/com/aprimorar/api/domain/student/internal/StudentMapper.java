package com.aprimorar.api.domain.student.internal;

import com.aprimorar.api.domain.parent.api.ParentService;
import com.aprimorar.api.domain.parent.api.dto.ParentResponseDTO;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.api.AddressMapper;
import com.aprimorar.api.domain.student.api.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.api.dto.StudentResponsibleSummaryDTO;

@Component
public class StudentMapper {

    private final Clock applicationClock;
    private final AddressMapper addressMapper;
    private final ObjectProvider<ParentService> parentService;

    public StudentMapper(Clock applicationClock, AddressMapper addressMapper, ObjectProvider<ParentService> parentService) {
        this.applicationClock = applicationClock;
        this.addressMapper = addressMapper;
        this.parentService = parentService;
    }

    public StudentResponseDTO convertToDto(Student entity) {
        ParentResponseDTO parent = parentService.getObject().findById(entity.getParentId());
        return convertToDto(entity, parent);
    }

    public StudentResponseDTO convertToDto(Student entity, ParentResponseDTO parent) {
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
            parent.parentId(),
            convertResponsibleSummary(parent),
            entity.getArchivedAt(),
            entity.getUpdatedAt(),
            entity.getCreatedAt()
        );
    }

    private StudentResponsibleSummaryDTO convertResponsibleSummary(ParentResponseDTO parent) {
        return new StudentResponsibleSummaryDTO(
            parent.parentId(),
            parent.name(),
            parent.contact(),
            parent.cpf()
        );
    }

    public Integer calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now(applicationClock);
        return Period.between(birthdate, today).getYears();
    }
}
