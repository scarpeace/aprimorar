package aprimorar.agendamento.atendimentos_individuais.domain;

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
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimento;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import lombok.Getter;

@Entity
@Getter
@Table(name = "atendimentos_individuais")
public class AtendimentoIndividual implements Serializable {

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
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoIndividual() {}

    public AtendimentoIndividual(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        Aluno aluno,
        Colaborador colaborador
    ) {
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.status = StatusAtendimentoIndividual.AGENDADO;
        this.aluno = aluno;
        this.colaborador = colaborador;
    }

    public AtendimentoIndividual update(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        Aluno aluno,
        Colaborador colaborador
    ) {
        validarPodeEditar();
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.aluno = aluno;
        this.colaborador = colaborador;
        this.tipo = tipo;
        return this;
    }

    //TODO: isso nao deveria ser public
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

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
