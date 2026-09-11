package aprimorar.atendimentos.cobranca_aluno.domain;

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
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cobrancas_alunos")
public class CobrancaAlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id")
    private Long atendimentoId;

    @Column(name = "aluno_id")
    private UUID alunoId;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusCobrancaAluno status;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 40)
    private FormaPagamento formaPagamento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected CobrancaAlunoEntity() {}

    public CobrancaAlunoEntity(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        validarValor(valor);
        this.atendimentoId = atendimentoId;
        this.alunoId = alunoId;
        this.valor = valor;
        this.status = StatusCobrancaAluno.PENDENTE;
    }

    public void registrarPagamento(FormaPagamento formaPagamento, String comprovanteUrl) {
        if (status == StatusCobrancaAluno.PAGO) {
            throw new IllegalStateException("Pagamento já está pago");
        }
        validarFormaPagamento(formaPagamento);

        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusCobrancaAluno.PAGO;
    }

    public void cancelarPagamento() {
        this.status = StatusCobrancaAluno.PENDENTE;
        this.dataPagamento = null;
        this.formaPagamento = null;
        this.comprovanteUrl = null;
    }

    public void update(UUID alunoId, BigDecimal valor) {
        if (Objects.equals(this.alunoId, alunoId)
            && this.valor != null
            && this.valor.compareTo(valor) == 0) {
            return;
        }
        if (status == StatusCobrancaAluno.PAGO) {
            throw new IllegalStateException("Não é possível alterar uma cobrança já paga");
        }
        validarValor(valor);
        this.alunoId = alunoId;
        this.valor = valor;
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor do pagamento é obrigatório");
        }
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser positivo");
        }
    }

    private static void validarFormaPagamento(FormaPagamento formaPagamento) {
        if (formaPagamento == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória");
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
