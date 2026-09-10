package aprimorar.financeiro.despesas.repository.specifications;

import aprimorar.financeiro.despesas.domain.Despesa;
import aprimorar.financeiro.despesas.enums.CategoriaDespesa;
import aprimorar.financeiro.despesas.enums.FormaPagamento;
import aprimorar.financeiro.despesas.web.dto.DespesaFiltroRequest;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public final class DespesaSpecifications {

    private DespesaSpecifications() {}

    public static Specification<Despesa> comFiltros(DespesaFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(categoriaIgual(filtro.categoria()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataMaiorOuIgual(filtro.dataInicio()))
            .and(dataMenorOuIgual(filtro.dataFim()));
    }

    public static Specification<Despesa> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("titulo")), pattern),
                cb.like(cb.lower(root.get("descricao")), pattern)
            );
        };
    }

    public static Specification<Despesa> categoriaIgual(CategoriaDespesa categoria) {
        return (root, query, cb) -> categoria == null ? null : cb.equal(root.get("categoria"), categoria);
    }

    public static Specification<Despesa> formaPagamentoIgual(FormaPagamento formaPagamento) {
        return (root, query, cb) -> formaPagamento == null ? null : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    public static Specification<Despesa> dataMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, cb) -> dataInicio == null ? null : cb.greaterThanOrEqualTo(root.get("dataPagamento"), dataInicio);
    }

    public static Specification<Despesa> dataMenorOuIgual(LocalDate dataFim) {
        return (root, query, cb) -> dataFim == null ? null : cb.lessThanOrEqualTo(root.get("dataPagamento"), dataFim);
    }
}
