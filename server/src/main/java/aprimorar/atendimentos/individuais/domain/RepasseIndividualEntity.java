package aprimorar.atendimentos.individuais.domain;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.domain.enums.StatusRepasseIndividual;
import aprimorar.atendimentos.individuais.domain.exception.RepasseIndividualDadosInvalidosException;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "repasses_individuais")
public class RepasseIndividualEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id", nullable = false)
    private Long atendimentoId;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusRepasseIndividual status;

    @Column(name = "data_repasse")
    private LocalDateTime dataRepasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 40)
    private FormaPagamento formaPagamento;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected RepasseIndividualEntity() {}

    public RepasseIndividualEntity(Long atendimentoId, UUID colaboradorId, BigDecimal valor) {
        validarValor(valor);
        this.atendimentoId = atendimentoId;
        this.colaboradorId = colaboradorId;
        this.valor = valor;
        this.status = StatusRepasseIndividual.PENDENTE;
    }

    public void registrarRepasse(FormaPagamento formaPagamento, String comprovanteUrl) {
        if (status == StatusRepasseIndividual.PAGO) {
            throw new RepasseIndividualDadosInvalidosException("Repasse já está pago");
        }
        if (formaPagamento == null) {
            throw new RepasseIndividualDadosInvalidosException("Forma de pagamento é obrigatória");
        }

        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
        this.dataRepasse = LocalDateTime.now();
        this.status = StatusRepasseIndividual.PAGO;
    }

    public void cancelarRepasse() {
        if (status != StatusRepasseIndividual.PAGO) {
            throw new RepasseIndividualDadosInvalidosException(
                "O repasse precisa estar pago para ser cancelado"
            );
        }

        this.status = StatusRepasseIndividual.PENDENTE;
        this.dataRepasse = null;
        this.formaPagamento = null;
        this.comprovanteUrl = null;
    }

    public void update(UUID colaboradorId, BigDecimal valor) {
        validarValor(valor);

        if (status == StatusRepasseIndividual.PAGO) {
            throw new RepasseIndividualDadosInvalidosException(
                "Não é possível alterar um repasse já pago"
            );
        }
        this.colaboradorId = colaboradorId;
        this.valor = valor;
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new RepasseIndividualDadosInvalidosException("Valor do repasse é obrigatório");
        }
        if (valor.signum() < 0) {
            throw new RepasseIndividualDadosInvalidosException("Valor do repasse não pode ser negativo");
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
