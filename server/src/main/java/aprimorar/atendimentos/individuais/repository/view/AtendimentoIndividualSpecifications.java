package aprimorar.atendimentos.individuais.repository.view;

import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.domain.enums.TipoAtendimento;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CobrancaIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseIndividualFiltroRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
            .and(anoMesEntre(filtro.anoMes()))
            .and(inicioMaiorOuIgual(filtro.anoMes() == null ? filtro.inicio() : null))
            .and(fimMenorOuIgual(filtro.anoMes() == null ? filtro.fim() : null))
            .and(tipoIgual(filtro.tipo()))
            .and(alunoIdIgual(filtro.alunoId()))
            .and(colaboradorIdIgual(filtro.colaboradorId()))
            .and(valorIgual("cobrancaStatus", filtro.statusCobranca()));
    }

    public static Specification<AtendimentoIndividualViewEntity> paraCobrancas(
        CobrancaIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(valorIgual("alunoId", filtro.alunoId()))
            .and(valorIgual("cobrancaStatus", nome(filtro.status())))
            .and(valorIgual("cobrancaFormaPagamento", nome(filtro.formaPagamento())))
            .and(dataMaiorOuIgual("cobrancaDataPagamento", filtro.dataPagamentoInicio()))
            .and(dataMenorOuIgual("cobrancaDataPagamento", filtro.dataPagamentoFim()));
    }

    public static Specification<AtendimentoIndividualViewEntity> paraRepasses(
        RepasseIndividualFiltroRequest filtro
    ) {
        return Specification
            .where(valorIgual("colaboradorId", filtro.colaboradorId()))
            .and(valorIgual("repasseStatus", nome(filtro.status())))
            .and(valorIgual("repasseFormaPagamento", nome(filtro.formaPagamento())))
            .and(dataMaiorOuIgual("repasseDataRepasse", filtro.dataRepasseInicio()))
            .and(dataMenorOuIgual("repasseDataRepasse", filtro.dataRepasseFim()));
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

    private static Specification<AtendimentoIndividualViewEntity> anoMesEntre(YearMonth anoMes) {
        return (root, query, cb) -> {
            if (anoMes == null) {
                return null;
            }

            LocalDateTime inicio = anoMes.atDay(1).atStartOfDay();
            LocalDateTime fim = anoMes.atEndOfMonth().atTime(LocalTime.MAX);
            return cb.and(
                cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicio),
                cb.lessThanOrEqualTo(root.get("dataHoraFim"), fim)
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

    private static Specification<AtendimentoIndividualViewEntity> tipoIgual(TipoAtendimento tipo) {
        return valorIgual("tipo", tipo);
    }

    private static Specification<AtendimentoIndividualViewEntity> alunoIdIgual(UUID alunoId) {
        return valorIgual("alunoId", alunoId);
    }

    private static Specification<AtendimentoIndividualViewEntity> colaboradorIdIgual(UUID colaboradorId) {
        return valorIgual("colaboradorId", colaboradorId);
    }

    private static Specification<AtendimentoIndividualViewEntity> valorIgual(String campo, Object valor) {
        return (root, query, cb) -> valor == null ? null : cb.equal(root.get(campo), valor);
    }

    private static String nome(Enum<?> valor) {
        return valor == null ? null : valor.name();
    }

    private static Specification<AtendimentoIndividualViewEntity> dataMaiorOuIgual(String campo, LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.greaterThanOrEqualTo(root.<LocalDateTime>get(campo), data.atStartOfDay());
    }

    private static Specification<AtendimentoIndividualViewEntity> dataMenorOuIgual(String campo, LocalDate data) {
        return (root, query, cb) -> data == null
            ? null
            : cb.lessThanOrEqualTo(root.<LocalDateTime>get(campo), data.atTime(LocalTime.MAX));
    }
}
