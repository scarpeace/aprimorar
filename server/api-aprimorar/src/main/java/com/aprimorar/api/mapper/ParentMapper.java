package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.util.MapperUtils;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {MapperUtils.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface ParentMapper {

    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    Parent toEntity(CreateParentDTO dto);

    @Mapping(target = "cpf", qualifiedByName = "formatCpf")
    @Mapping(target = "contact", qualifiedByName = "formatContact")
    ParentResponseDTO toDto(Parent entity);

    ParentSummaryDTO toSummaryDto(Parent entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cpf", qualifiedByName = "sanitizeCpf")
    @Mapping(target = "email", qualifiedByName = "sanitizeEmail")
    @Mapping(target = "contact", qualifiedByName = "sanitizeContact")
    void updateFromDto(CreateParentDTO dto, @MappingTarget Parent entity);
}
