package aprimorar.agendamento.colaboradores.web.dto;

import aprimorar.agendamento.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados resumidos do colaborador para listagem")
public record ColaboradorListResponse(
    UUID id,
    String nome,
    String cpf,
    String telefone,
    FuncoesColaborador funcao,
    boolean ativo,
    LocalDateTime createdAt
) {

    public static ColaboradorListResponse toDto(Colaborador colaborador) {
        return new ColaboradorListResponse(
            colaborador.getId(),
            colaborador.getNome(),
            colaborador.getCpf(),
            colaborador.getTelefone(),
            colaborador.getFuncao(),
            colaborador.getActive(),
            colaborador.getCreatedAt()
        );
    }
}
