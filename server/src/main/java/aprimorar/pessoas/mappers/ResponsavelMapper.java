package aprimorar.pessoas.mappers;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponsavelMapper {

    public Responsavel toEntity(ResponsavelRequestDTO dto) {
        return new Responsavel(
            dto.nome(),
            dto.dataNascimento(),
            dto.telefone(),
            dto.cpf(),
            dto.email()
        );
    }

    public ResponsavelResponseDTO toResponseDto(Responsavel responsavel) {
        return new ResponsavelResponseDTO(
            responsavel.getId(),
            responsavel.getNome(),
            responsavel.getDataNascimento(),
            responsavel.getCpf(),
            responsavel.getTelefone(),
            responsavel.getEmail(),
            responsavel.getCreatedAt(),
            responsavel.getUpdatedAt()
        );
    }
}
