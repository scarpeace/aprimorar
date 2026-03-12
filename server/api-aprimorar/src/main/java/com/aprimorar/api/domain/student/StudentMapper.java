package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
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

    public StudentEntity convertToEntity(StudentRequestDTO dto) {
 
        StudentEntity entity = new StudentEntity();
        entity.setName(dto.name());
        entity.setBirthdate(dto.birthdate());
        entity.setCpf(MapperUtils.sanitizeCpf(dto.cpf()));
        entity.setSchool(dto.school());
        entity.setContact(MapperUtils.sanitizeContact(dto.contact()));
        entity.setEmail(MapperUtils.sanitizeEmail(dto.email()));
        entity.setAddressEntity(addressMapper.convertToEntity(dto.address()));
        entity.setParentEntity(parentMapper.convertToEntity(dto.parent()));
        return entity;
    }

    public StudentResponseDTO convertToDto(StudentEntity entity) {

        return new StudentResponseDTO(
                entity.getId(),
                entity.getName(),
                MapperUtils.formatContact(entity.getContact()),
                entity.getEmail(),
                MapperUtils.formatCpf(entity.getCpf()),
                entity.getBirthdate(),
                entity.getSchool(),
                calculateAge(entity.getBirthdate()),
                addressMapper.convertToDto(entity.getAddressEntity()),
                parentMapper.convertToDto(entity.getParentEntity()),
                entity.getCreatedAt(),
                entity.getArchivedAt()
        );
    }


    public StudentEntity updateToEntity(UpdateStudentDTO dto) {
       
        StudentEntity entity = new StudentEntity();
        entity.setName(dto.name());
        entity.setBirthdate(dto.birthdate());
        entity.setCpf(MapperUtils.sanitizeCpf(dto.cpf()));
        entity.setSchool(dto.school());
        entity.setContact(MapperUtils.sanitizeContact(dto.contact()));
        entity.setEmail(MapperUtils.sanitizeEmail(dto.email()));
        entity.setAddressEntity(addressMapper.convertToEntity(dto.address()));
        entity.setParentEntity(parentMapper.convertToEntity(dto.parent()));
        return entity;
    }

    private Integer calculateAge(LocalDate birthdate) {
        if (birthdate == null) {
            return null;
        }

        LocalDate todayInSaoPaulo = LocalDate.now(applicationClock);
        return Period.between(birthdate, todayInSaoPaulo).getYears();
    }
}
