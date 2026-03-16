package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.Parent;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class StudentMapper {

    private final Clock applicationClock;
    private final AddressMapper addressMapper;

    public StudentMapper(
            Clock applicationClock,
            AddressMapper addressMapper
    ) {
        this.applicationClock = applicationClock;
        this.addressMapper = addressMapper;
    }

    public Student convertToEntity(StudentRequestDTO dto) {
        Student student = new Student();

        student.setName(dto.name());
        student.setBirthdate(dto.birthdate());
        student.setCpf(MapperUtils.normalizeCpf(dto.cpf()));
        student.setSchool(dto.school());
        student.setContact(MapperUtils.normalizeContact(dto.contact()));
        student.setEmail(MapperUtils.normalizeEmail(dto.email()));
        student.setAddress(addressMapper.convertToEntity(dto.address()));

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
                entity.getAddress(),
                entity.getParent(),
                entity.getArchivedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedAt()
        );
    }

    private Integer calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now(applicationClock);
        return Period.between(birthdate, today).getYears();
    }
}
