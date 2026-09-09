package aprimorar.atendimentos.individuais.repository.specifications;

import aprimorar.atendimentos.individuais.domain.AtendimentoEntity;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.individuais.domain.StatusAtendimento;
import aprimorar.atendimentos.individuais.domain.enums.TipoAtendimento;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<AtendimentoEntity> comFiltros(AtendimentoFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(anoMesEntre(filtro.anoMes()))
            .and(inicioMaiorOuIgual(filtro.anoMes() == null ? filtro.inicio() : null))
            .and(fimMenorOuIgual(filtro.anoMes() == null ? filtro.fim() : null))
            .and(statusIgual(filtro.status()))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()));
    }

    public static Specification<AtendimentoEntity> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("tipo").as(String.class)), pattern);
        };
    }

    public static Specification<AtendimentoEntity> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null ? null : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    public static Specification<AtendimentoEntity> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null ? null : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    public static Specification<AtendimentoEntity> anoMesEntre(YearMonth anoMes) {
        return (root, query, cb) -> {
            if (anoMes == null) {
                return null;
            }

            var inicio = anoMes.atDay(1).atStartOfDay();
            var fim = anoMes.atEndOfMonth().atTime(23, 59, 59, 999_999_999);

            return cb.and(
                cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio),
                cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim)
            );
        };
    }

    public static Specification<AtendimentoEntity> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    public static Specification<AtendimentoEntity> statusIgual(StatusAtendimento status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<AtendimentoEntity> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<AtendimentoEntity> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaboradorId"), colaboradorId);
    }
}
