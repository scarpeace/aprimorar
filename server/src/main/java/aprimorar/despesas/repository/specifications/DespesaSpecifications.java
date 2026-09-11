package aprimorar.despesas.repository.specifications;

import aprimorar.despesas.domain.DespesaEntity;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import aprimorar.despesas.web.dto.DespesaFiltroRequest;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public final class DespesaSpecifications {

    private DespesaSpecifications() {}

    public static Specification<DespesaEntity> comFiltros(DespesaFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(categoriaIgual(filtro.categoria()))
            .and(formaPagamentoIgual(filtro.formaPagamento()))
            .and(dataMaiorOuIgual(filtro.dataInicio()))
            .and(dataMenorOuIgual(filtro.dataFim()));
    }

    public static Specification<DespesaEntity> buscaContem(String termo) {
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

    public static Specification<DespesaEntity> categoriaIgual(CategoriaDespesa categoria) {
        return (root, query, cb) -> categoria == null ? null : cb.equal(root.get("categoria"), categoria);
    }

    public static Specification<DespesaEntity> formaPagamentoIgual(FormaPagamento formaPagamento) {
        return (root, query, cb) -> formaPagamento == null ? null : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

    public static Specification<DespesaEntity> dataMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, cb) -> dataInicio == null ? null : cb.greaterThanOrEqualTo(root.get("dataPagamento"), dataInicio);
    }

    public static Specification<DespesaEntity> dataMenorOuIgual(LocalDate dataFim) {
        return (root, query, cb) -> dataFim == null ? null : cb.lessThanOrEqualTo(root.get("dataPagamento"), dataFim);
    }
}
