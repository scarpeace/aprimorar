package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.student.command.StudentCommand;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class StudentMapper {

    private final ParentMapper parentMapper;
    private final AddressMapper addressMapper;
    private final Clock applicationClock;

    public StudentMapper(
            ParentMapper parentMapper,
            AddressMapper addressMapper,
            Clock applicationClock
    ) {
        this.parentMapper = parentMapper;
        this.addressMapper = addressMapper;
        this.applicationClock = applicationClock;
    }

    public StudentCommand convertToCommand(StudentRequestDTO dto) {
        return new StudentCommand(
                dto.name(),
                dto.birthdate(),
                dto.cpf(),
                dto.school(),
                dto.contact(),
                dto.email(),
                addressMapper.convertToEntity(dto.address()),
                dto.parentRequestDTO()
        );
    }


    public StudentResponseDTO convertToDto(Student entity) {

        return new StudentResponseDTO(
                entity.getId(),
                entity.getName(),
                MapperUtils.formatContact(entity.getContact()),
                entity.getEmail(),
                MapperUtils.formatCpf(entity.getCpf()),
                entity.getBirthdate(),
                entity.getSchool(),
                calculateAge(entity.getBirthdate()),
                addressMapper.convertToDto(entity.getAddress()),
                parentMapper.convertToDto(entity.getParent()),
                entity.getCreatedAt(),
                entity.getArchivedAt()
        );
    }

    private Integer calculateAge(LocalDate birthdate) {
        if (birthdate == null) {
            return null;
        }

        LocalDate today = LocalDate.now(applicationClock);
        return Period.between(birthdate, today).getYears();
    }
}
