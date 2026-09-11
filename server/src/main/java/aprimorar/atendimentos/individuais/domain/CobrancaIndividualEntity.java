package aprimorar.atendimentos.individuais.domain;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.domain.enums.StatusCobrancaIndividual;
import aprimorar.atendimentos.individuais.domain.exception.CobrancaIndividualDadosInvalidosException;
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
@Table(name = "cobrancas_individuais")
public class CobrancaIndividualEntity {

    private static final BigDecimal VALOR_MINIMO = BigDecimal.valueOf(50);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atendimento_id", nullable = false)
    private Long atendimentoId;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusCobrancaIndividual status;

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

    protected CobrancaIndividualEntity() {}

    public CobrancaIndividualEntity(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        validarValor(valor);
        this.atendimentoId = atendimentoId;
        this.alunoId = alunoId;
        this.valor = valor;
        this.status = StatusCobrancaIndividual.PENDENTE;
    }

    public void registrarPagamento(FormaPagamento formaPagamento, String comprovanteUrl) {
        if (status == StatusCobrancaIndividual.PAGO) {
            throw new CobrancaIndividualDadosInvalidosException("Cobrança já está paga");
        }
        if (formaPagamento == null) {
            throw new CobrancaIndividualDadosInvalidosException("Forma de pagamento é obrigatória");
        }

        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusCobrancaIndividual.PAGO;
    }

    public void cancelarPagamento() {
        if (status != StatusCobrancaIndividual.PAGO) {
            throw new CobrancaIndividualDadosInvalidosException(
                "A cobrança precisa estar paga para ser cancelada"
            );
        }

        this.status = StatusCobrancaIndividual.PENDENTE;
        this.dataPagamento = null;
        this.formaPagamento = null;
        this.comprovanteUrl = null;
    }

    public void update(UUID alunoId, BigDecimal valor) {
        validarValor(valor);

        if (status == StatusCobrancaIndividual.PAGO) {
            throw new CobrancaIndividualDadosInvalidosException(
                "Não é possível alterar uma cobrança já paga"
            );
        }
        this.alunoId = alunoId;
        this.valor = valor;
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new CobrancaIndividualDadosInvalidosException("Valor da cobrança é obrigatório");
        }
        if (valor.compareTo(VALOR_MINIMO) < 0) {
            throw new CobrancaIndividualDadosInvalidosException(
                "O valor da cobrança não pode ser menor que R$50,00"
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
