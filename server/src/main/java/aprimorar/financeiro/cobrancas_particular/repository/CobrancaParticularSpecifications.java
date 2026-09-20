package aprimorar.financeiro.cobrancas_particular.repository;

import aprimorar.financeiro.cobrancas_particular.domain.CobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.domain.enums.StatusCobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.web.dto.CobrancaParticularFiltroRequest;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CobrancaParticularSpecifications {

    private CobrancaParticularSpecifications() {
    }

    public static Specification<CobrancaParticular> comFiltros(
        CobrancaParticularFiltroRequest filtro
    ) {
        return Specification.allOf(
            alunoIdIgual(filtro.alunoId()),
            statusIgual(filtro.status()),
            formaPagamentoIgual(filtro.formaPagamento()),
            dataRecebimentoMaiorOuIgual(filtro.dataRecebimentoInicio()),
            dataRecebimentoMenorOuIgual(filtro.dataRecebimentoFim())
        );
    }

    private static Specification<CobrancaParticular> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null
            ? null
            : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<CobrancaParticular> statusIgual(
        StatusCobrancaParticular status
    ) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            if (status == StatusCobrancaParticular.ATRASADA) {
                return cb.and(
                    cb.equal(root.get("status"), StatusCobrancaParticular.PENDENTE),
                    cb.lessThan(
                        root.get("createdAt"),
                        LocalDateTime.now().minusDays(30)
                    ),
                    cb.isNull(root.get("recebimento"))
                );
            }

            return cb.equal(root.get("status"), status);
        };
    }

    private static Specification<CobrancaParticular> formaPagamentoIgual(
        FormaPagamentoEnum formaPagamento
    ) {
        return (root, query, cb) -> formaPagamento == null
            ? null
            : cb.equal(root.join("recebimento").get("formaPagamento"), formaPagamento);
    }

    private static Specification<CobrancaParticular> dataRecebimentoMaiorOuIgual(
        LocalDate dataRecebimentoInicio
    ) {
        return (root, query, cb) -> dataRecebimentoInicio == null
            ? null
            : cb.greaterThanOrEqualTo(
                root.join("recebimento").get("dataRecebimento"),
                dataRecebimentoInicio
            );
    }

    private static Specification<CobrancaParticular> dataRecebimentoMenorOuIgual(
        LocalDate dataRecebimentoFim
    ) {
        return (root, query, cb) -> dataRecebimentoFim == null
            ? null
            : cb.lessThanOrEqualTo(
                root.join("recebimento").get("dataRecebimento"),
                dataRecebimentoFim
            );
    }
}
