package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.employee.CreateEmployeeDTO;
import com.aprimorar.api.dto.employee.EmployeeResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.util.MapperUtils;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {MapperUtils.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    Employee toEntity(CreateEmployeeDTO dto);

    @Mapping(target = "birthdate", qualifiedByName = "parseBirthdate")
    @Mapping(target = "cpf", qualifiedByName = "formatCpf")
    @Mapping(target = "contact", qualifiedByName = "formatContact")
    EmployeeResponseDTO toDto(Employee entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "birthdate", qualifiedByName = "formatBirthdate")
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    void updateFromDto(CreateEmployeeDTO dto, @MappingTarget Employee entity);
}
