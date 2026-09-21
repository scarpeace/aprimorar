package aprimorar.agendamento.atendimentos_particular.domain;

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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.atendimentos_particular.domain.enums.StatusAtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.TipoAtendimentoParticular;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularDadosInvalidosException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularEdicaoExpiradaException;
import lombok.Getter;

@Entity
@Getter
@Table(name = "atendimentos_particular")
public class AtendimentoParticular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAtendimentoParticular tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAtendimentoParticular status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoParticular() {}

    public AtendimentoParticular(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimentoParticular tipo,
        Aluno aluno,
        Colaborador colaborador
    ) {
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.status = StatusAtendimentoParticular.AGENDADO;
        this.aluno = aluno;
        this.colaborador = colaborador;
    }

    public void atualizar(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimentoParticular tipo,
        Aluno aluno,
        Colaborador colaborador
    ) {
        exigirStatusAgendado("Só é possível editar um atendimento agendado");
        validarJanelaDeEdicao();
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.aluno = aluno;
        this.colaborador = colaborador;
        this.tipo = tipo;
    }

    public void realizar() {
        exigirStatusAgendado("Só é possível realizar um atendimento agendado");
        this.status = StatusAtendimentoParticular.REALIZADO;
    }

    public void cancelar() {
        exigirStatusAgendado("Só é possível cancelar um atendimento agendado");
        this.status = StatusAtendimentoParticular.CANCELADO;
    }


    private void validarJanelaDeEdicao() {
        if (this.dataHoraFim != null && LocalDateTime.now().isAfter(this.dataHoraFim.plus(20, ChronoUnit.DAYS))) {
            throw new AtendimentoParticularEdicaoExpiradaException();
        }
    }

    private void exigirStatusAgendado(String message) {
        if (this.status != StatusAtendimentoParticular.AGENDADO) {
            throw new AtendimentoParticularDadosInvalidosException(message);
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
