package aprimorar.financeiro.recebimentos_alunos.domain;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoDadosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cobranca_recebimentos")
public class CobrancaRecebimento {

    @Id
    private UUID id;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 40)
    private FormaPagamentoEnum formaPagamento;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recebimento", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Cobranca> cobrancas = new ArrayList<>();

    protected CobrancaRecebimento() {
    }

    public CobrancaRecebimento(
        LocalDate dataRecebimento,
        BigDecimal valorTotal,
        FormaPagamentoEnum formaPagamento,
        String comprovanteUrl
    ) {
        validarDataRecebimento(dataRecebimento);
        this.id = UUID.randomUUID();
        this.dataRecebimento = dataRecebimento;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
    }

    private static void validarDataRecebimento(LocalDate dataRecebimento) {
        if (dataRecebimento != null && dataRecebimento.isAfter(LocalDate.now())) {
            throw new RecebimentoDadosInvalidosException(
                "A data do recebimento não pode ser futura"
            );
        }
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
