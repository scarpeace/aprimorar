package aprimorar.pessoas.aluno.application;

import aprimorar.pessoas.aluno.web.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.AlunoResponseDTO;
import aprimorar.pessoas.aluno.domain.Aluno;
import aprimorar.pessoas.common.address.AddressMapper;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno toEntity(AlunoRequestDTO dto) {
        return new Aluno(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
                dto.cpf(),
            dto.email(),
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
