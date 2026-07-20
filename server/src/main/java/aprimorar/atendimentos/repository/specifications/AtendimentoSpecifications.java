package aprimorar.atendimentos.repository.specifications;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<Atendimento> comFiltros(AtendimentoFiltroRequest filtro) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(anoMesEntre(filtro.anoMes()))
            .and(inicioMaiorOuIgual(filtro.anoMes() == null ? filtro.inicio() : null))
            .and(fimMenorOuIgual(filtro.anoMes() == null ? filtro.fim() : null))
            .and(statusIgual(filtro.status()))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(alunoNomeContem(filtro.alunoNome()))
            .and(colaboradorIdIgual(filtro.colaboradorId()))
            .and(colaboradorNomeContem(filtro.colaboradorNome()));
    }

    public static Specification<Atendimento> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + termo.trim().toLowerCase() + "%";
            var aluno = root.join("aluno", JoinType.INNER);
            var colaborador = root.join("colaborador", JoinType.INNER);

            return cb.or(
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern),
                cb.like(cb.lower(aluno.get("nome")), pattern),
                cb.like(cb.lower(colaborador.get("nome")), pattern)
            );
        };
    }

    public static Specification<Atendimento> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null ? null : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    public static Specification<Atendimento> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null ? null : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    public static Specification<Atendimento> anoMesEntre(YearMonth anoMes) {
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

    public static Specification<Atendimento> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> alunoId == null ? null : cb.equal(root.get("aluno").get("id"), alunoId);
    }

    public static Specification<Atendimento> alunoNomeContem(String nomeAluno) {
        return (root, query, cb) -> {
            if (nomeAluno == null || nomeAluno.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + nomeAluno.trim().toLowerCase() + "%";
            var aluno = root.join("aluno", JoinType.INNER);

            return cb.like(cb.lower(aluno.get("nome")), pattern);
        };
    }

    public static Specification<Atendimento> statusIgual(StatusAtendimento status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Atendimento> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<Atendimento> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> colaboradorId == null ? null : cb.equal(root.get("colaborador").get("id"), colaboradorId);
    }

    public static Specification<Atendimento> colaboradorNomeContem(String colaboradorNome) {
        return (root, query, cb) -> {
            if (colaboradorNome == null || colaboradorNome.trim().isEmpty()) {
                return null;
            }

            String pattern = "%" + colaboradorNome.trim().toLowerCase() + "%";
            var colaborador = root.join("colaborador", JoinType.INNER);

            return cb.like(cb.lower(colaborador.get("nome")), pattern);
        };
    }
}
