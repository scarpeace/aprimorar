package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.student.dto.StudentCreateDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.StudentUpdateDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class StudentMapper {

    private final Clock applicationClock;
    private final AddressMapper addressMapper;
    private final ParentMapper parentMapper;

    public StudentMapper(
            Clock applicationClock,
            AddressMapper addressMapper,
            ParentMapper parentMapper
    ) {
        this.applicationClock = applicationClock;
        this.addressMapper = addressMapper;
        this.parentMapper = parentMapper;
    }

    public Student convertToEntityForCreate(StudentCreateDTO dto) {
        Student student = new Student();

        student.setName(dto.name());
        student.setBirthdate(dto.birthdate());
        student.setCpf(MapperUtils.normalizeCpf(dto.cpf()));
        student.setSchool(dto.school());
        student.setContact(MapperUtils.normalizeContact(dto.contact()));
        student.setEmail(MapperUtils.normalizeEmail(dto.email()));
        student.setAddress(addressMapper.convertToEntity(dto.address()));
        student.setParent(parentMapper.convertToEntityForCreate(dto.parent()));

        return student;
    }

public Student convertToEntityForUpdate(StudentUpdateDTO dto) {
        Student student = new Student();

        student.setName(dto.name());
        student.setBirthdate(dto.birthdate());
        student.setSchool(dto.school());
        student.setContact(MapperUtils.normalizeContact(dto.contact()));
        student.setEmail(MapperUtils.normalizeEmail(dto.email()));
        student.setAddress(addressMapper.convertToEntity(dto.address()));
        student.setParent(parentMapper.convertToEntityForUpdate(dto.parent()));

        return student;
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
                parentMapper.convertToDto(entity.getParent()),
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
