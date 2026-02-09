package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.AddressResponseDto;
import com.aprimorar.api.controller.dto.ParentResponseDto;
import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        uses = {ParentMapper.class, AddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface StudentMapper {

    Student toEntity(StudentRequestDto dto);

    StudentReponseDto toDto(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateFromDto(StudentRequestDto dto, @MappingTarget Student entity);
}
