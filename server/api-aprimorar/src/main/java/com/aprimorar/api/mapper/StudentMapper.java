package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.util.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    private final ParentMapper parentMapper;
    private final AddressMapper addressMapper;
    private final MapperUtils mapperUtils;

    public StudentMapper(ParentMapper parentMapper, AddressMapper addressMapper, MapperUtils mapperUtils) {
        this.parentMapper = parentMapper;
        this.addressMapper = addressMapper;
        this.mapperUtils = mapperUtils;
    }

    public Student toEntity(CreateStudentDTO dto) {
        if (dto == null) {
            return null;
        }

        Student entity = new Student();
        entity.setName(dto.name());
        entity.setBirthdate(dto.birthdate());
        entity.setCpf(mapperUtils.sanitizeCpf(dto.cpf()));
        entity.setSchool(dto.school());
        entity.setContact(mapperUtils.sanitizeContact(dto.contact()));
        entity.setEmail(mapperUtils.sanitizeEmail(dto.email()));
        entity.setActivity(dto.activity());
        entity.setAddress(addressMapper.toEntity(dto.address()));
        entity.setParent(parentMapper.toEntity(dto.parent()));
        return entity;
    }

    public StudentResponseDTO toDto(Student entity) {
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
                entity.getActivity(),
                entity.getActive(),
                addressMapper.toDto(entity.getAddress()),
                parentMapper.toDto(entity.getParent()),
                entity.getCreatedAt()
        );
    }

    public void updateFromDto(CreateStudentDTO dto, Student entity) {
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
        if (dto.activity() != null) {
            entity.setActivity(dto.activity());
        }
        if (dto.address() != null) {
            entity.setAddress(addressMapper.toEntity(dto.address()));
        }
        if (dto.parent() != null) {
            entity.setParent(parentMapper.toEntity(dto.parent()));
        }
    }
}
