package aprimorar.atendimentos.repository.specifications;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import aprimorar.atendimentos.domain.Transacao;
import aprimorar.atendimentos.dto.TransacaoFiltroRequest;
import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;

public final class TransacaoSpecifications {
    public static Specification<Transacao> comFiltros(TransacaoFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(pagadorIdIgual(filtro.pagadorId()))
            .and(recebedorIdIgual(filtro.recebedorId()))
            .and(dataEfetivadaContem(filtro.dataEfetivada()))
            .and(tipoIgual(filtro.tipo()))
            .and(categoriaIgual(filtro.categoria()))
            .and(statusIgual(filtro.status()))
            .and(formaPagamentoIgual(filtro.formaPagamento()));
    }

    public static Specification<Transacao> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
            );
        };
    }

    public static Specification<Transacao> pagadorIdIgual(UUID pagadorId) {
        return (root, query, cb) -> pagadorId == null ? null : cb.equal(root.get("pagadorId"), pagadorId);
    }

    public static Specification<Transacao> recebedorIdIgual(UUID recebedorId) {
        return (root, query, cb) -> recebedorId == null ? null : cb.equal(root.get("recebedorId"), recebedorId);
    }

    public static Specification<Transacao> dataEfetivadaContem(LocalDateTime data) {
        return (root, query, cb) -> {
            if (data == null) {
                return null;
            }

            String pattern = "%" + data.toString().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("dataEfetivada").as(String.class)), pattern)
            );
        };
    }

    public static Specification<Transacao> tipoIgual(TipoTransacao tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<Transacao> categoriaIgual(CategoriaTransacao categoria) {
        return (root, query, cb) -> categoria == null ? null : cb.equal(root.get("categoria"), categoria);
    }

    public static Specification<Transacao> statusIgual(StatusTransacao status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Transacao> formaPagamentoIgual(FormaPagamento formaPagamento) {
        return (root, query, cb) -> formaPagamento == null ? null : cb.equal(root.get("formaPagamento"), formaPagamento);
    }

}
