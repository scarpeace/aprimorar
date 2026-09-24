package aprimorar.financeiro.recebimentos_alunos.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um recebimento de cobranças")
public record RecebimentoDetalheResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

    @NotNull
    @Schema(nullable = false)
    LocalDate dataRecebimento,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valorTotal,

    @NotNull
    @Schema(nullable = false)
    FormaPagamentoEnum formaPagamento,

    @Nullable
    @Schema(nullable = true)
    String comprovanteUrl,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime createdAt,

    @NotNull
    @Schema(nullable = false)
    List<CobrancaResponse> cobrancas
) {
    public static RecebimentoDetalheResponse toDto(
        CobrancaRecebimento recebimento
    ) {
        List<Cobranca> cobrancas = recebimento.getCobrancas();

        return new RecebimentoDetalheResponse(
            recebimento.getId(),
            cobrancas.getFirst().getAlunoId(),
            recebimento.getDataRecebimento(),
            recebimento.getValorTotal(),
            recebimento.getFormaPagamento(),
            recebimento.getComprovanteUrl(),
            recebimento.getCreatedAt(),
            cobrancas.stream().map(CobrancaResponse::toDto).toList()
        );
    }
}
