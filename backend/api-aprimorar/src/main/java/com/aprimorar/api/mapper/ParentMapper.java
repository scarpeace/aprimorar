package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.ParentRequestDto;
import com.aprimorar.api.controller.dto.ParentResponseDto;
import com.aprimorar.api.entity.Parent;

public class ParentMapper {

    public static ParentResponseDto toDto(Parent parent){
        return new ParentResponseDto(
                parent.getName(),
                parent.getEmail());
    }

}
