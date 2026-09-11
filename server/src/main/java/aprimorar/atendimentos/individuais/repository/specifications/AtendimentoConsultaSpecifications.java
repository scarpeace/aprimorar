package aprimorar.atendimentos.individuais.repository.specifications;

import aprimorar.atendimentos.individuais.domain.AtendimentoConsultaViewEntity;
import aprimorar.atendimentos.individuais.enums.TipoAtendimento;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoFiltroRequest;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoConsultaSpecifications {

    private AtendimentoConsultaSpecifications() {
    }

    public static Specification<AtendimentoConsultaViewEntity> comFiltros(
        AtendimentoFiltroRequest filtro
    ) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(anoMesEntre(filtro.anoMes()))
            .and(inicioMaiorOuIgual(filtro.anoMes() == null ? filtro.inicio() : null))
            .and(fimMenorOuIgual(filtro.anoMes() == null ? filtro.fim() : null))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()))
            .and(statusCobrancaIgual(filtro.statusCobranca()));
    }

    private static Specification<AtendimentoConsultaViewEntity> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("alunoNome")), pattern),
                cb.like(cb.lower(root.get("colaboradorNome")), pattern),
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
            );
        };
    }

    private static Specification<AtendimentoConsultaViewEntity> anoMesEntre(YearMonth anoMes) {
        return (root, query, cb) -> {
            if (anoMes == null) {
                return null;
            }

            LocalDateTime inicio = anoMes.atDay(1).atStartOfDay();
            LocalDateTime fim = anoMes.atEndOfMonth().atTime(23, 59, 59, 999_999_999);
            return cb.and(
                cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio),
                cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim)
            );
        };
    }

    private static Specification<AtendimentoConsultaViewEntity> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    private static Specification<AtendimentoConsultaViewEntity> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    private static Specification<AtendimentoConsultaViewEntity> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<AtendimentoConsultaViewEntity> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<AtendimentoConsultaViewEntity> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    private static Specification<AtendimentoConsultaViewEntity> statusCobrancaIgual(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("cobrancaStatus"), status);
    }
}
