package aprimorar.instituicao.atendimentos.repository.specifications;

import aprimorar.instituicao.alunos.domain.AlunoEntity;
import aprimorar.instituicao.atendimentos.domain.AtendimentoIndividualEntity;
import aprimorar.instituicao.atendimentos.domain.enums.TipoAtendimento;
import aprimorar.instituicao.atendimentos.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.instituicao.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.instituicao.colaboradores.domain.ColaboradorEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class AtendimentoIndividualSpecifications {

    private AtendimentoIndividualSpecifications() {
    }

    public static Specification<AtendimentoIndividualEntity> paraAtendimentos(
        AtendimentoIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(buscaContem(filtro.busca()))
            .and(inicioMaiorOuIgual(filtro.inicio()))
            .and(fimMenorOuIgual(filtro.fim()))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()));
    }

    public static Specification<AtendimentoIndividualEntity> paraCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return Specification
            .where(dataHoraFimMaiorOuIgual(filtro.inicio()))
            .and(dataHoraInicioMenorOuIgual(filtro.fim()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()));
    }

    private static Specification<AtendimentoIndividualEntity> buscaContem(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return null;
            }

            Join<AtendimentoIndividualEntity, AlunoEntity> aluno = root.join("aluno", JoinType.INNER);
            Join<AtendimentoIndividualEntity, ColaboradorEntity> colaborador =
                root.join("colaborador", JoinType.INNER);
            String pattern = "%" + termo.trim().toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(aluno.get("nome")), pattern),
                cb.like(cb.lower(colaborador.get("nome")), pattern),
                cb.like(cb.lower(root.get("tipo").as(String.class)), pattern)
            );
        };
    }

    private static Specification<AtendimentoIndividualEntity> inicioMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio);
    }

    private static Specification<AtendimentoIndividualEntity> fimMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim);
    }

    private static Specification<AtendimentoIndividualEntity> dataHoraFimMaiorOuIgual(LocalDateTime inicio) {
        return (root, query, cb) -> inicio == null
            ? null
            : cb.greaterThanOrEqualTo(root.get("dataHoraFim"), inicio);
    }

    private static Specification<AtendimentoIndividualEntity> dataHoraInicioMenorOuIgual(LocalDateTime fim) {
        return (root, query, cb) -> fim == null
            ? null
            : cb.lessThanOrEqualTo(root.get("dataHoraInicio"), fim);
    }

    private static Specification<AtendimentoIndividualEntity> tipoIgual(TipoAtendimento tipo) {
        return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    private static Specification<AtendimentoIndividualEntity> alunoIdIgual(UUID alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }

            Join<AtendimentoIndividualEntity, AlunoEntity> aluno = root.join("aluno", JoinType.INNER);
            return cb.equal(aluno.get("id"), alunoId);
        };
    }

    private static Specification<AtendimentoIndividualEntity> colaboradorIdIgual(UUID colaboradorId) {
        return (root, query, cb) -> {
            if (colaboradorId == null) {
                return null;
            }

            Join<AtendimentoIndividualEntity, ColaboradorEntity> colaborador =
                root.join("colaborador", JoinType.INNER);
            return cb.equal(colaborador.get("id"), colaboradorId);
        };
    }
}
