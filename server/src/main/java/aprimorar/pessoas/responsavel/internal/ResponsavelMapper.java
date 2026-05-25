package aprimorar.pessoas.responsavel.internal;

import aprimorar.pessoas.responsavel.api.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.api.dto.ResponsavelResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponsavelMapper {

    public Responsavel toEntity(ResponsavelRequestDTO dto) {
        return new Responsavel(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.cpf(),
            dto.email()
        );
    }

    public ResponsavelResponseDTO toResponseDto(Responsavel parent) {
        return new ResponsavelResponseDTO(
            parent.getId(),
            parent.getName(),
            parent.getBirthdate(),
            parent.getCpf(),
            parent.getContact(),
            parent.getEmail(),
            parent.getPix(),
            parent.getActive(),
            parent.getCreatedAt(),
            parent.getUpdatedAt()
        );
    }
}
