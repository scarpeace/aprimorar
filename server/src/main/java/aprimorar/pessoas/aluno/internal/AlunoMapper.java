package aprimorar.pessoas.aluno.internal;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.pessoas.shared.address.AddressMapper;

@Component
public class AlunoMapper {

    public Aluno toEntity(AlunoRequestDTO dto) {
        return new Aluno(
                dto.name(),
                dto.birthdate(),
                dto.pix(),
                dto.contact(),
                dto.email(),
                dto.cpf(),
                dto.school(),
                dto.parentId(),
                AddressMapper.toEntity(dto.address())
        );
    }

    public AlunoResponseDTO toResponseDto(Aluno student, Clock clock) {
        return new AlunoResponseDTO(
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
                AddressMapper.toDto(student.getAddress())
        );
    }

//TODO: Talvez esse método não precise receber um clock
public Integer calculateAge(Aluno student, Clock clock) {
    LocalDate today = LocalDate.now(clock);
    return Period.between(student.getBirthdate(), today).getYears();
}

}
