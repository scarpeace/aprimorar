package aprimorar.pessoas.colaborador.internal;

import org.springframework.stereotype.Component;

import aprimorar.pessoas.colaborador.api.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.shared.address.AddressMapper;


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
            AddressMapper.toEntity(dto.address())
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
            AddressMapper.toDto(employee.getAddress()),
            employee.getActive(),
            employee.getCreatedAt(),
            employee.getUpdatedAt()
        );
    }
}
