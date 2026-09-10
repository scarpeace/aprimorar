package aprimorar.financeiro.cobranca_aluno.web.dto;

import aprimorar.financeiro.cobranca_aluno.api.FormaPagamento;
import aprimorar.financeiro.cobranca_aluno.domain.CobrancaAlunoEntity;
import aprimorar.financeiro.cobranca_aluno.domain.StatusCobrancaAluno;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Cobrança de aluno")
public record CobrancaAlunoResponse(
    Long id,
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor,
    BigDecimal desconto,
    StatusCobrancaAluno status,
    LocalDateTime dataPagamento,
    FormaPagamento formaPagamento,
    String comprovanteUrl
) {
    public static CobrancaAlunoResponse from(CobrancaAlunoEntity entity) {
        return new CobrancaAlunoResponse(
            entity.getId(),
            entity.getAtendimentoId(),
            entity.getAlunoId(),
            entity.getValor(),
            entity.getDesconto(),
            entity.getStatus(),
            entity.getDataPagamento(),
            entity.getFormaPagamento(),
            entity.getComprovanteUrl()
        );
    }
}
