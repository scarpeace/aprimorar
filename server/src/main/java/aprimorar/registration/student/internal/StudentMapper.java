package aprimorar.registration.student.internal;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import aprimorar.registration.student.api.dto.StudentResponseDTO;

@Component
public class StudentMapper {

    public StudentResponseDTO toResponseDto(Student student, Clock clock) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getContact(),
                student.getEmail(),
                student.getCpf(),
                student.getBirthdate(),
                student.getSchool(),
                calculateAge(student, clock),
                student.getParentId(),
                student.getActive(),
                student.getUpdatedAt(),
                student.getCreatedAt(),
                student.getAddress().toResponseDto()
        );
    }

//TODO: Talvez esse método não precise receber um clock
public Integer calculateAge(Student student, Clock clock) {
    LocalDate today = LocalDate.now(clock);
    return Period.between(student.getBirthdate(), today).getYears();
}

}
