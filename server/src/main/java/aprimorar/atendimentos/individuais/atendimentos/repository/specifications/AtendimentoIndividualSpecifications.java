package aprimorar.atendimentos.individuais.atendimentos.repository.specifications;

import aprimorar.atendimentos.individuais.atendimentos.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.atendimentos.domain.enums.TipoAtendimento;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import java.time.LocalDateTime;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoIndividualSpecifications {

    private AtendimentoIndividualSpecifications() {
    }

    public static Specification<AtendimentoIndividualViewEntity> paraAtendimentos(
        AtendimentoIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(inicioMaiorOuIgual(filtro.inicio()))
            .and(fimMenorOuIgual(filtro.fim()))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()))
            .and(statusCobrancaIgual(filtro.statusCobranca()))
            .and(statusRepasseIgual(filtro.statusRepasse()));
    }

    public static Specification<AtendimentoIndividualViewEntity> paraCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return Specification
            .where(dataHoraFimMaiorOuIgual(filtro.inicio()))
            .and(dataHoraInicioMenorOuIgual(filtro.fim()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()));
    }


    private static Specification<AtendimentoIndividualViewEntity> buscaContem(String termo) {
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


    private static Specification<AtendimentoIndividualViewEntity> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    private static Specification<AtendimentoIndividualViewEntity> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    private static Specification<AtendimentoIndividualViewEntity> dataHoraFimMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraFim"), inicio);
    }

    private static Specification<AtendimentoIndividualViewEntity> dataHoraInicioMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraInicio"), fim);
    }

    private static Specification<AtendimentoIndividualViewEntity> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<AtendimentoIndividualViewEntity> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("alunoId"), alunoId);
    }

    private static Specification<AtendimentoIndividualViewEntity> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null
            ? null
            : cb.equal(root.get("colaboradorId"), colaboradorId);
    }

    private static Specification<AtendimentoIndividualViewEntity> statusCobrancaIgual(String statusCobranca) {
        return (root, query, cb) -> statusCobranca == null
            ? null
            : cb.equal(root.get("cobrancaStatus"), statusCobranca);
    }

    private static Specification<AtendimentoIndividualViewEntity> statusRepasseIgual(String statusRepasse) {
        return (root, query, cb) -> statusRepasse == null
            ? null
            : cb.equal(root.get("repasseStatus"), statusRepasse);
    }

}
