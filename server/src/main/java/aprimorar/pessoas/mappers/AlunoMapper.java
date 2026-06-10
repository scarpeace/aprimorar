package aprimorar.pessoas.mappers;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.AlunoRequestDTO;
import aprimorar.pessoas.dto.AlunoResponseDTO;
import aprimorar.pessoas.shared.address.BrazilianStatesEnum;
import aprimorar.pessoas.shared.address.dto.AddressResponseDTO;

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
            toEndereco(dto)
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
            toAddressResponseDto(student.getEndereco())
        );
    }

    public Endereco toEndereco(AlunoRequestDTO dto) {
        var address = dto.address();
        return new Endereco(
            address.street(),
            "0",
            address.district(),
            address.city(),
            address.state().name(),
            address.zip(),
            address.complement()
        );
    }

    private AddressResponseDTO toAddressResponseDto(Endereco endereco) {
        return new AddressResponseDTO(
            endereco.getRua(),
            endereco.getBairro(),
            endereco.getCidade(),
            BrazilianStatesEnum.valueOf(endereco.getEstado()),
            endereco.getCep(),
            endereco.getComplemento()
        );
    }

    //TODO: Talvez esse método não precise receber um clock
    public Integer calculateAge(Aluno student, Clock clock) {
        LocalDate today = LocalDate.now(clock);
        return Period.between(student.getBirthdate(), today).getYears();
    }
}
