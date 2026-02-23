package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.entity.Event;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface EventMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.name", target = "employeeName")
    EventResponseDTO toDto(Event entity);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "id", ignore = true)
    Event toEntity(CreateEventDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(CreateEventDTO dto, @MappingTarget Event entity);
}

