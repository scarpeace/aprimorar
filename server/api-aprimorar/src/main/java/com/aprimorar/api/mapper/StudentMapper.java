package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.entity.Student;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        uses = {ParentMapper.class, AddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)

public interface StudentMapper {

    //Entity -> DTO
    Student toEntity(CreateStudentDTO dto);

    //DTO - Entity
    StudentResponseDTO toDto(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateStudentDTO dto, @MappingTarget Student entity);
}
