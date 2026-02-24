package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.util.MapperUtils;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {ParentMapper.class, AddressMapper.class, MapperUtils.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface StudentMapper {

    //Entity -> DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    Student toEntity(CreateStudentDTO dto);

    //DTO - Entity
    @Mapping(target = "contact", qualifiedByName = "formatContact")
    @Mapping(target = "cpf", qualifiedByName = "formatCpf")
    StudentResponseDTO toDto(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    void updateFromDto(CreateStudentDTO dto, @MappingTarget Student entity);
}
