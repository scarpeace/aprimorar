package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.StudentResponseDto;
import com.aprimorar.api.controller.dto.CreateStudentDto;
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
    Student toEntity(CreateStudentDto dto);

    //DTO - Entity
    StudentResponseDto toDto(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(CreateStudentDto dto, @MappingTarget Student entity);
}
