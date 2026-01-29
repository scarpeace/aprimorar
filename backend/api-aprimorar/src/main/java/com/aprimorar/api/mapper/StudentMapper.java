package com.aprimorar.api.mapper;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;

public class StudentMapper {
    public static StudentReponseDto toDto(Student student) {
        return new StudentReponseDto(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getSchool(),
                student.getPhone(),
                student.getBirthdate(),
                student.getActivity(),
                student.getCreatedAt()
        );
    }


    public static Student toEntity(StudentRequestDto studentRequestDto) {
        Address addressEntity = AddressMapper.toEntity(studentRequestDto.addressRequestDto());
        Parent parentEntity = ParentMapper.toEntity(studentRequestDto.parentRequestDto());

        Student studentEntity = new Student();

        studentEntity.setName(studentRequestDto.name());
        studentEntity.setCpf(studentRequestDto.cpf());
        studentEntity.setBirthdate(studentRequestDto.birthdate());
        studentEntity.setPhone(studentRequestDto.phone());
        studentEntity.setActive(true);
        studentEntity.setSchool(studentRequestDto.school());
        studentEntity.setActivity(studentRequestDto.activity());

        studentEntity.setParent(parentEntity);
        studentEntity.setAddress(addressEntity);

        return studentEntity;


    }
}
