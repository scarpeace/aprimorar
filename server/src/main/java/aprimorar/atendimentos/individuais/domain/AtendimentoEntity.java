package aprimorar.atendimentos.individuais.domain;

import aprimorar.atendimentos.individuais.domain.exception.AtendimentoDadosInvalidosException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoEdicaoExpiradaException;
import aprimorar.atendimentos.individuais.enums.TipoAtendimento;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.Getter;

@Entity
@Getter
@Table(name = "atendimentos")
public class AtendimentoEntity implements Serializable {

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

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "pagamento_aluno", precision = 10, scale = 2, nullable = false)
    private BigDecimal pagamentoAluno;

    @Column(name = "repasse_colaborador", precision = 10, scale = 2, nullable = false)
    private BigDecimal repasseColaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoEntity() {}

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public AtendimentoEntity(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        UUID alunoId,
        UUID colaboradorId,
        BigDecimal pagamentoAluno,
        BigDecimal repasseColaborador
    ) {
        validarDatas(dataHoraInicio, dataHoraFim);
        validarValores(pagamentoAluno, repasseColaborador);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
        this.pagamentoAluno = pagamentoAluno;
        this.repasseColaborador = repasseColaborador;
    }

    public AtendimentoEntity update(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        UUID alunoId,
        UUID colaboradorId,
        BigDecimal pagamentoAluno,
        BigDecimal repasseColaborador
    ) {
        validarJanelaEdicao();
        validarDatas(dataHoraInicio, dataHoraFim);
        validarValores(pagamentoAluno, repasseColaborador);
        reagendar(dataHoraInicio, dataHoraFim);
        alterarParticipantes(alunoId, colaboradorId);
        this.tipo = tipo;
        this.pagamentoAluno = pagamentoAluno;
        this.repasseColaborador = repasseColaborador;
        return this;
    }


    public void reagendar(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){

        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    public void alterarParticipantes(UUID alunoId, UUID colaboradorId){
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
    }

    public void validarJanelaEdicao() {
        if (this.dataHoraFim != null && LocalDateTime.now().isAfter(this.dataHoraFim.plus(20, ChronoUnit.DAYS))) {
            throw new AtendimentoEdicaoExpiradaException();
        }
    }

    private void validarDatas(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraFim.isBefore(dataHoraInicio)) {
            throw new AtendimentoDadosInvalidosException("Data de fim do atendimento nao pode ser anterior a data de inicio");
        }
    }

    private void validarValores(BigDecimal pagamentoAluno, BigDecimal repasseColaborador) {
        if (pagamentoAluno.compareTo(repasseColaborador) < 0) {
            throw new AtendimentoDadosInvalidosException("O valor do atendimento nao pode ser menor que o pagamento");
        }
        if (pagamentoAluno.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new AtendimentoDadosInvalidosException("O valor do atendimento nao pode ser menor que R$50,00");
        }
    }
}
