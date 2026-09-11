package aprimorar.atendimentos.individuais.web.dto.cobranca;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.domain.CobrancaIndividualEntity;
import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.domain.enums.StatusCobrancaIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Cobrança de aluno")
public record CobrancaIndividualResponse(
    Long id,
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor,
    StatusCobrancaIndividual status,
    LocalDateTime dataPagamento,
    FormaPagamento formaPagamento,
    String comprovanteUrl
) {
    public static CobrancaIndividualResponse toDto(CobrancaIndividualEntity entity) {
        return new CobrancaIndividualResponse(
            entity.getId(),
            entity.getAtendimentoId(),
            entity.getAlunoId(),
            entity.getValor(),
            entity.getStatus(),
            entity.getDataPagamento(),
            entity.getFormaPagamento(),
            entity.getComprovanteUrl()
        );
    }

    public static CobrancaIndividualResponse toDto(AtendimentoIndividualViewEntity view) {
        return new CobrancaIndividualResponse(
            view.getCobrancaId(),
            view.getId(),
            view.getAlunoId(),
            view.getCobrancaValor(),
            StatusCobrancaIndividual.valueOf(view.getCobrancaStatus()),
            view.getCobrancaDataPagamento(),
            view.getCobrancaFormaPagamento() == null
                ? null
                : FormaPagamento.valueOf(view.getCobrancaFormaPagamento()),
            view.getCobrancaComprovanteUrl()
        );
    }
}
