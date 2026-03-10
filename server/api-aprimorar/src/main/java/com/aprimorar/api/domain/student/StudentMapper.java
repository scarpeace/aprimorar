package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.student.dto.CreateStudentDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class StudentMapper {

    private final ParentMapper parentMapper;
    private final AddressMapper addressMapper;
    private final MapperUtils mapperUtils;
    private final Clock ageCalculationClock;

    public StudentMapper(
            ParentMapper parentMapper,
            AddressMapper addressMapper,
            MapperUtils mapperUtils,
            Clock ageCalculationClock
    ) {
        this.parentMapper = parentMapper;
        this.addressMapper = addressMapper;
        this.mapperUtils = mapperUtils;
        this.ageCalculationClock = ageCalculationClock;
    }

    public StudentEntity toEntity(CreateStudentDTO dto) {
        if (dto == null) {
            return null;
        }

        StudentEntity entity = new StudentEntity();
        entity.setName(dto.name());
        entity.setBirthdate(dto.birthdate());
        entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        entity.setSchool(dto.school());
        entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        entity.setAddress(addressMapper.toEntity(dto.address()));
        entity.setParent(parentMapper.toEntity(dto.parent()));
        return entity;
    }

    public StudentResponseDTO toDto(StudentEntity entity) {
        if (entity == null) {
            return null;
        }

        return new StudentResponseDTO(
                entity.getId(),
                entity.getName(),
                mapperUtils.formatContact(entity.getContact()),
                entity.getEmail(),
                mapperUtils.formatCpf(entity.getCpf()),
                entity.getBirthdate(),
                entity.getSchool(),
                calculateAge(entity.getBirthdate()),
                entity.getArchivedAt(),
                entity.getLastReactivatedAt(),
                addressMapper.toDto(entity.getAddress()),
                parentMapper.toDto(entity.getParent()),
                entity.getCreatedAt()
        );
    }

    public void updateFromDto(UpdateStudentDTO dto, StudentEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.birthdate() != null) {
            entity.setBirthdate(dto.birthdate());
        }
        if (dto.cpf() != null) {
            entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        }
        if (dto.school() != null) {
            entity.setSchool(dto.school());
        }
        if (dto.contact() != null) {
            entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        }
        if (dto.email() != null) {
            entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        }
        if (dto.address() != null) {
            updateAddress(dto, entity);
        }
        if (dto.parent() != null) {
            updateParent(dto, entity);
        }
    }

    private void updateAddress(UpdateStudentDTO dto, StudentEntity entity) {
        if (entity.getAddress() == null) {
            entity.setAddress(addressMapper.toEntity(dto.address()));
            return;
        }

        addressMapper.updateFromDto(dto.address(), entity.getAddress());
    }

    private void updateParent(UpdateStudentDTO dto, StudentEntity entity) {
        if (entity.getParent() == null) {
            entity.setParent(parentMapper.toEntity(dto.parent()));
            return;
        }

        parentMapper.updateFromDto(dto.parent(), entity.getParent());
    }

    private Integer calculateAge(LocalDate birthdate) {
        if (birthdate == null) {
            return null;
        }

        LocalDate todayInSaoPaulo = LocalDate.now(ageCalculationClock);
        return Period.between(birthdate, todayInSaoPaulo).getYears();
    }
}
