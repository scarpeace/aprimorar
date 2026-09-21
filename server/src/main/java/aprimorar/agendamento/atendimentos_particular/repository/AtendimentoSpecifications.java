package aprimorar.agendamento.atendimentos_particular.repository;

import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.atendimentos_particular.domain.AtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.StatusAtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.TipoAtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtendimentoParticularFiltroRequest;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoSpecifications {

    private AtendimentoSpecifications() {
    }

    public static Specification<AtendimentoParticular> comFiltros(
        AtendimentoParticularFiltroRequest filtro
    ) {
        return Specification.allOf(
            buscaContem(filtro.busca()),
            inicioMaiorOuIgual(filtro.inicio()),
            fimMenorOuIgual(filtro.fim()),
            tipoIgual(filtro.tipo()),
            statusIgual(filtro.status()),
            alunoIdIgual(filtro.alunoId()),
            colaboradorIdIgual(filtro.colaboradorId())
        );
    }

    private static Specification<AtendimentoParticular> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return null;
            }

            Join<AtendimentoParticular, Aluno> aluno = root.join("aluno", JoinType.INNER);
            Join<AtendimentoParticular, Colaborador> colaborador =
                root.join("colaborador", JoinType.INNER);
            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(aluno.get("nome")), pattern),
                cb.like(cb.lower(colaborador.get("nome")), pattern),
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
            );
        };
    }

    private static Specification<AtendimentoParticular> inicioMaiorOuIgual(
        LocalDateTime inicio
    ) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    private static Specification<AtendimentoParticular> fimMenorOuIgual(
        LocalDateTime fim
    ) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    private static Specification<AtendimentoParticular> tipoIgual(
        TipoAtendimentoParticular tipo
    ) {
        return (root, query, cb) -> tipo == null
            ? null
            : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<AtendimentoParticular> statusIgual(
        StatusAtendimentoParticular status
    ) {
        return (root, query, cb) -> status == null
            ? null
            : cb.equal(root.get("status"), status);
    }

    private static Specification<AtendimentoParticular> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }

            Join<AtendimentoParticular, Aluno> aluno = root.join("aluno", JoinType.INNER);
            return cb.equal(aluno.get("id"), alunoId);
        };
    }

    private static Specification<AtendimentoParticular> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> {
            if (colaboradorId == null) {
                return null;
            }

            Join<AtendimentoParticular, Colaborador> colaborador =
                root.join("colaborador", JoinType.INNER);
            return cb.equal(colaborador.get("id"), colaboradorId);
        };
    }
}
