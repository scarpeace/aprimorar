package aprimorar.atendimentos.individuais.domain;

import aprimorar.atendimentos.individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import aprimorar.atendimentos.individuais.domain.enums.TipoAtendimento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
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

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoIndividualEntity() {}

    public AtendimentoIndividualEntity(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        UUID alunoId,
        UUID colaboradorId
    ) {
        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
    }

    public AtendimentoIndividualEntity update(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        UUID alunoId,
        UUID colaboradorId
    ) {
        validarJanelaEdicao();
        validarDatas(dataHoraInicio, dataHoraFim);
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
        this.tipo = tipo;
        return this;
    }

    public void validarJanelaEdicao() {
        if (this.dataHoraFim != null && LocalDateTime.now().isAfter(this.dataHoraFim.plus(20, ChronoUnit.DAYS))) {
            throw new AtendimentoIndividualEdicaoExpiradaException();
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
