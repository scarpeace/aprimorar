package aprimorar.instituicao.atendimentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import aprimorar.instituicao.alunos.domain.AlunoEntity;
import aprimorar.instituicao.atendimentos.domain.enums.StatusAtendimentoIndividual;
import aprimorar.instituicao.atendimentos.domain.enums.TipoAtendimento;
import aprimorar.instituicao.colaboradores.domain.ColaboradorEntity;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import lombok.Getter;

@Entity
@Getter
@Table(name = "atendimentos_individuais")
public class AtendimentoIndividualEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAtendimento tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAtendimentoIndividual status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoEntity aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private ColaboradorEntity colaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoIndividualEntity() {}

    public AtendimentoIndividualEntity(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        AlunoEntity aluno,
        ColaboradorEntity colaborador
    ) {
        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.status = StatusAtendimentoIndividual.AGENDADO;
        this.aluno = aluno;
        this.colaborador = colaborador;
    }

    public AtendimentoIndividualEntity update(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        AlunoEntity aluno,
        ColaboradorEntity colaborador
    ) {
        validarPodeEditar();
        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.aluno = aluno;
        this.colaborador = colaborador;
        this.tipo = tipo;
        return this;
    }

    public void validarPodeEditar() {
        exigirStatusAgendado("Só é possível editar um atendimento agendado");
        validarJanelaEdicao();
    }

    public void realizar() {
        exigirStatusAgendado("Só é possível realizar um atendimento agendado");
        this.status = StatusAtendimentoIndividual.REALIZADO;
    }

    public void cancelar() {
        exigirStatusAgendado("Só é possível cancelar um atendimento agendado");
        this.status = StatusAtendimentoIndividual.CANCELADO;
    }

    public void validarJanelaEdicao() {
        if (this.dataHoraFim != null && LocalDateTime.now().isAfter(this.dataHoraFim.plus(20, ChronoUnit.DAYS))) {
            throw new AtendimentoIndividualEdicaoExpiradaException();
        }
    }

    private void exigirStatusAgendado(String mensagem) {
        if (this.status != StatusAtendimentoIndividual.AGENDADO) {
            throw new AtendimentoIndividualDadosInvalidosException(mensagem);
        }
    }

    private void validarDatas(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraFim == null || dataHoraInicio == null || dataHoraFim.isBefore(dataHoraInicio)) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Data de fim do atendimento nao pode ser anterior à data de inicio"
            );
        }
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
