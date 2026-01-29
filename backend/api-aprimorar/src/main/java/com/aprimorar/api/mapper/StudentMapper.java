package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;

public class StudentMapper {
    public static StudentReponseDto toDto(Student student) {
        return new StudentReponseDto(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getSchool(),
                student.getBirthdate(),
                student.getActivity(),
                student.getCreatedAt()
        );
    }


}
