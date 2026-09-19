package aprimorar.instituicao.atendimentos_individuais.repository;

import aprimorar.instituicao.alunos.domain.Aluno;
import aprimorar.instituicao.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.instituicao.atendimentos_individuais.domain.enums.TipoAtendimento;
import aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.instituicao.atendimentos_individuais.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.instituicao.colaboradores.domain.Colaborador;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoIndividualSpecifications {

    private AtendimentoIndividualSpecifications() {
    }

    public static Specification<AtendimentoIndividual> paraAtendimentos(
        AtendimentoIndividualFiltroRequest filtro
    ) {
        return Specification.allOf(
            buscaContem(filtro.busca()),
            inicioMaiorOuIgual(filtro.inicio()),
            fimMenorOuIgual(filtro.fim()),
            tipoIgual(filtro.tipo()),
            alunoIdIgual(filtro.alunoId()),
            colaboradorIdIgual(filtro.colaboradorId())
        );
    }

    public static Specification<AtendimentoIndividual> paraCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return Specification.allOf(
            dataHoraFimMaiorOuIgual(filtro.inicio()),
            dataHoraInicioMenorOuIgual(filtro.fim()),
            alunoIdIgual(filtro.alunoId()),
            colaboradorIdIgual(filtro.colaboradorId())
        );
    }

    private static Specification<AtendimentoIndividual> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return null;
            }

            Join<AtendimentoIndividual, Aluno> aluno = root.join("aluno", JoinType.INNER);
            Join<AtendimentoIndividual, Colaborador> colaborador =
                root.join("colaborador", JoinType.INNER);
            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(aluno.get("nome")), pattern),
                cb.like(cb.lower(colaborador.get("nome")), pattern),
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
            );
        };
    }

    private static Specification<AtendimentoIndividual> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    private static Specification<AtendimentoIndividual> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    private static Specification<AtendimentoIndividual> dataHoraFimMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraFim"), inicio);
    }

    private static Specification<AtendimentoIndividual> dataHoraInicioMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraInicio"), fim);
    }

    private static Specification<AtendimentoIndividual> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<AtendimentoIndividual> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }

            Join<AtendimentoIndividual, Aluno> aluno = root.join("aluno", JoinType.INNER);
            return cb.equal(aluno.get("id"), alunoId);
        };
    }

    private static Specification<AtendimentoIndividual> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> {
            if (colaboradorId == null) {
                return null;
            }

            Join<AtendimentoIndividual, Colaborador> colaborador =
                root.join("colaborador", JoinType.INNER);
            return cb.equal(colaborador.get("id"), colaboradorId);
        };
    }
}
