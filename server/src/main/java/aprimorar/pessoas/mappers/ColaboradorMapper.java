package aprimorar.pessoas.mappers;

import org.springframework.stereotype.Component;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.events.ColaboradorResponseDTO;
import aprimorar.pessoas.shared.address.BrazilianStatesEnum;
import aprimorar.pessoas.shared.address.dto.AddressResponseDTO;


@Component
public class ColaboradorMapper {

    public Colaborador toEntity(ColaboradorRequestDTO dto) {
        return new Colaborador(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.cpf(),
            dto.email(),
            dto.duty(),
            toEndereco(dto)
        );
    }

    public ColaboradorResponseDTO toDto(Colaborador employee) {
        return new ColaboradorResponseDTO(
            employee.getId(),
            employee.getName(),
            employee.getBirthdate(),
            employee.getPix(),
            employee.getContact(),
            employee.getCpf(),
            employee.getEmail(),
            employee.getDuty(),
            toAddressResponseDto(employee.getEndereco()),
            employee.getActive(),
            employee.getCreatedAt(),
            employee.getUpdatedAt()
        );
    }

    public Endereco toEndereco(ColaboradorRequestDTO dto) {
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
}
