package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.shared.MapperUtils;

@Component
public class StudentMapper {

    private final Clock applicationClock;

    public StudentMapper(
            Clock applicationClock
    ) {
        this.applicationClock = applicationClock;
    }

    public Student convertToEntity(StudentRequestDTO dto) {
        Student student = new Student();

        student.setName(dto.name());
        student.setBirthdate(dto.birthdate());
        student.setCpf(MapperUtils.normalizeCpf(dto.cpf()));
        student.setSchool(dto.school());
        student.setContact(MapperUtils.normalizeContact(dto.contact()));
        student.setEmail(MapperUtils.normalizeEmail(dto.email()));
        student.setAddress(dto.address());
        student.setParent(dto.parent());

        return student;

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
