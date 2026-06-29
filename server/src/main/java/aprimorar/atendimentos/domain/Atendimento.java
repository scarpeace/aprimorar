package aprimorar.atendimentos.domain;

import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Entity
@Getter
@Table(name = "atendimentos")
public class Atendimento implements Serializable {

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
    private StatusAtendimento status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Column(name = "pagamento_aluno", precision = 10, scale = 2, nullable = false)
    private BigDecimal pagamentoAluno;

    @Column(name = "repasse_colaborador", precision = 10, scale = 2, nullable = false)
    private BigDecimal repasseColaborador;

    @Column(name = "data_pagamento_aluno")
    private LocalDateTime dataPagamentoAluno;

    @Column(name = "data_pagamento_colaborador")
    private LocalDateTime dataPagamentoColaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Atendimento() {}

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Atendimento(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        Aluno aluno,
        Colaborador colaborador,
        BigDecimal pagamentoAluno,
        BigDecimal repasseColaborador
    ) {
        validarDatas(dataHoraInicio, dataHoraFim);
        validarValores(pagamentoAluno, repasseColaborador);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.status = StatusAtendimento.AGENDADO;
        this.aluno = aluno;
        this.colaborador = colaborador;
        this.pagamentoAluno = pagamentoAluno;
        this.repasseColaborador = repasseColaborador;
    }

    public Atendimento update(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        Aluno aluno,
        Colaborador colaborador,
        BigDecimal pagamentoAluno,
        BigDecimal repasseColaborador
    ) {
        validarDatas(dataHoraInicio, dataHoraFim);
        validarValores(pagamentoAluno, repasseColaborador);
        reagendar(dataHoraInicio, dataHoraFim);
        alterarParticipantes(aluno, colaborador);
        this.tipo = tipo;
        this.pagamentoAluno = pagamentoAluno;
        this.repasseColaborador = repasseColaborador;
        return this;
    }

    public void cancelar(){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Evento já cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível cancelar um evento concluído");
        }
        this.status = StatusAtendimento.CANCELADO;
    }

    public void concluir(){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível concluir um evento cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Evento já concluido");
        }

        this.status = StatusAtendimento.CONCLUIDO;
    }

    public void reagendar(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível reagendar um evento cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível reagendar um evento concluído");
        }

        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    public void alterarParticipantes(Aluno aluno, Colaborador colaborador){
        this.aluno = aluno;
        this.colaborador = colaborador;
    }

    @Transient
    public java.util.UUID getAlunoId() {
        return aluno.getId();
    }

    @Transient
    public java.util.UUID getColaboradorId() {
        return colaborador.getId();
    }

    public void validarJanelaEdicao() {
        if (this.dataHoraFim != null && LocalDateTime.now().isAfter(this.dataHoraFim.plus(20, ChronoUnit.DAYS))) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A janela de 20 dias para editar as informações do atendimento encerrou");
        }
    }

    private void validarDatas(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraFim.isBefore(dataHoraInicio)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode ser anterior a data de inicio");
        }
        if (dataHoraFim.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Data de fim do atendimento nao pode estar no passado");
        }
    }

    private void validarValores(BigDecimal pagamentoAluno, BigDecimal repasseColaborador) {
        if (pagamentoAluno.compareTo(repasseColaborador) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que o pagamento");
        }
        if (pagamentoAluno.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que R$50,00");
        }
    }
}
