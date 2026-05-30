package aprimorar.pessoas.responsavel.internal.application;

import aprimorar.pessoas.responsavel.internal.web.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.internal.web.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.responsavel.internal.domain.Responsavel;
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

    public ResponsavelResponseDTO toResponseDto(Responsavel responsavel) {
        return new ResponsavelResponseDTO(
            responsavel.getId(),
            responsavel.getName(),
            responsavel.getBirthdate(),
            responsavel.getCpf(),
            responsavel.getContact(),
            responsavel.getEmail(),
            responsavel.getPix(),
            responsavel.getActive(),
            responsavel.getCreatedAt(),
            responsavel.getUpdatedAt()
        );
    }
}
